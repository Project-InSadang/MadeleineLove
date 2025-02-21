package sideproject.madeleinelove.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sideproject.madeleinelove.dto.BlackPostDto;
import sideproject.madeleinelove.dto.BlackRequestDto;
import sideproject.madeleinelove.entity.*;
import sideproject.madeleinelove.exception.PostErrorResult;
import sideproject.madeleinelove.exception.PostException;
import sideproject.madeleinelove.exception.UserErrorResult;
import sideproject.madeleinelove.exception.UserException;
import sideproject.madeleinelove.repository.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlackPostService {

    private final BlackPostRepository blackPostRepository;
    private final BlackLikeRepository blackLikeRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final TokenServiceImpl tokenServiceImpl;
    private final UserRepository userRepository;
    private final BlackLikeService blackLikeService;

    private static final Logger logger = LoggerFactory.getLogger(BlackPostService.class);

    public List<BlackPostDto> getPosts(String sort, String cursor, int size, String userId) {
        Pageable pageable = PageRequest.of(0, size + 1); // 다음 페이지 확인을 위해 size + 1

        List<BlackPost> posts;

        switch (sort.toLowerCase()) {
            case "best":
                // 좋아요 내림차순 상위 3개 => cursor / size 무시
                posts = blackPostRepository.findTop3ByOrderByLikeCountDesc();
                break;
            case "recommended":
                // 좋아요 기반 커서 페이지네이션
                posts = getPostsByLikesCount(cursor, pageable);
                break;
            default:
                // "latest" 등 -> 최신순 커서 페이지네이션
                posts = getPostsByLatest(cursor, pageable);
                break;
        }

        boolean hasNext = false;
        if (!"best".equalsIgnoreCase(sort)) {
            hasNext = (posts.size() > size);
            if (hasNext) {
                posts = posts.subList(0, size);
            }
        }

        // 사용자 좋아요 정보 가져오기
        Set<ObjectId> likedPostIds = getUserLikedPostIds(userId);

        // DTO 변환 및 isLiked 설정
        return posts.stream()
                .map(post -> convertToDto(post, likedPostIds))
                .collect(Collectors.toList());
    }

    private List<BlackPost> getPostsByLatest(String cursor, Pageable pageable) {
        if (cursor == null) {
            return blackPostRepository.findAllByOrderByPostIdDesc(pageable);
        } else {
            ObjectId cursorId = new ObjectId(cursor);
            return blackPostRepository.findByPostIdLessThanOrderByPostIdDesc(cursorId, pageable);
        }
    }

    private List<BlackPost> getPostsByLikesCount(String cursor, Pageable pageable) {
        if (cursor == null) {
            return blackPostRepository.findAllByOrderByLikeCountDescPostIdDesc(pageable);
        } else {
            String[] cursorParts = cursor.split("_");
            int likesCount = Integer.parseInt(cursorParts[0]);
            ObjectId postId = new ObjectId(cursorParts[1]);

            return blackPostRepository.findByLikeCountLessThanEqualAndPostIdLessThanOrderByLikeCountDescPostIdDesc(
                    likesCount, postId, pageable
            );
        }
    }

    public String getNextCursor(List<BlackPostDto> dtos, String sort) {
        if (dtos.isEmpty()) {
            return null;
        }

        if ("best".equalsIgnoreCase(sort)) {
            return null;
        }

        BlackPostDto lastDto = dtos.get(dtos.size() - 1);

        if ("recommended".equalsIgnoreCase(sort)) {
            // likesCount_postId 형식의 커서 반환
            return String.format("%d_%s", lastDto.getLikeCount(), lastDto.getPostId());

            // hotScore_postId 형식의 커서 반환
            // return String.format("%f_%s", lastDto.getHotScore(), lastDto.getPostId().toHexString());
        } else {
            // postId를 커서로 반환
            return lastDto.getPostId();
        }
    }

    private Set<ObjectId> getUserLikedPostIds(String userId) {
        if (userId == null) {
            return Collections.emptySet();
        }
        List<BlackLike> likes = blackLikeRepository.findByUserId(userId);
        return likes.stream()
                .map(BlackLike::getPostId)
                .collect(Collectors.toSet());
    }

    private BlackPostDto convertToDto(BlackPost post, Set<ObjectId> likedPostIds) {
        BlackPostDto dto = new BlackPostDto();
        dto.setPostId(post.getPostId().toHexString());
        dto.setNickName(post.getNickName());
        dto.setContent(post.getContent());
        dto.setMethodNumber(post.getMethodNumber());
        dto.setLikeCount(post.getLikeCount());
        dto.setLikedByUser(likedPostIds.contains(post.getPostId()));
        // dto.setHotScore(post.getHotScore());
        return dto;
    }

    public void deleteBlackPost(HttpServletRequest request, HttpServletResponse response,
                                String accessToken, String stringPostId) {

        ObjectId userId = tokenServiceImpl.getUserIdFromAccessToken(request, response, accessToken);
        User existingUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserException(UserErrorResult.NOT_FOUND_USER));

        ObjectId postId;
        BlackPost blackPost;
        try {
            postId = new ObjectId(stringPostId);
            blackPost = blackPostRepository.findByPostId(postId)
                    .orElseThrow(() -> new PostException(PostErrorResult.NOT_FOUND_POST));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid post id: " + stringPostId);
        }

        if (!blackPost.getUserId().equals(userId)) {
            throw new PostException(PostErrorResult.UNAUTHORIZED_ACCESS);
        }

        blackLikeService.removeAllBlackLikesForPost(postId);
        blackPostRepository.delete(blackPost);
        deletePostLikesFromRedis(postId);
    }

    public void deletePostLikesFromRedis(ObjectId postId) {
        try {
            String key = "blackpost:" + postId + ":likes";
            redisTemplate.delete(key);
        } catch (Exception e) {
            // Log the error without interrupting the main flow
            System.err.println("Failed to delete likes from Redis for postId: " + postId);
            e.printStackTrace();
        }
    }

    public void saveBlackPost(HttpServletRequest request, HttpServletResponse response,
                              String accessToken, BlackRequestDto blackRequestDto) {

        ObjectId userId = tokenServiceImpl.getUserIdFromAccessToken(request, response, accessToken);
        User existingUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserException(UserErrorResult.NOT_FOUND_USER));

        BlackPost blackPost = createBlackPost(userId, blackRequestDto);
        blackPostRepository.save(blackPost);
    }

    private BlackPost createBlackPost(ObjectId userId, BlackRequestDto blackRequestDto) {
        return BlackPost.builder()
                .postId(new ObjectId())
                .userId(userId)
                .nickName(blackRequestDto.getNickName())
                .content(blackRequestDto.getContent())
                .methodNumber(blackRequestDto.getMethodNumber())
                .likeCount(0)
                .createdAt(LocalDateTime.now())
                .build();
    }
}