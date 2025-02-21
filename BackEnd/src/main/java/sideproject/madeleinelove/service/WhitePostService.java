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
import sideproject.madeleinelove.dto.WhitePostDto;
import sideproject.madeleinelove.entity.User;
import sideproject.madeleinelove.entity.WhiteLike;
import sideproject.madeleinelove.entity.WhitePost;
import sideproject.madeleinelove.exception.PostErrorResult;
import sideproject.madeleinelove.exception.PostException;
import sideproject.madeleinelove.exception.UserErrorResult;
import sideproject.madeleinelove.exception.UserException;
import sideproject.madeleinelove.repository.UserRepository;
import sideproject.madeleinelove.repository.WhiteLikeRepository;
import sideproject.madeleinelove.repository.WhitePostRepository;
import sideproject.madeleinelove.dto.WhiteRequestDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WhitePostService {

    private final WhitePostRepository whitePostRepository;
    private final WhiteLikeRepository whiteLikeRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final TokenServiceImpl tokenServiceImpl;
    private final UserRepository userRepository;
    private final WhiteLikeService whiteLikeService;

    private static final Logger logger = LoggerFactory.getLogger(WhitePostService.class);

    public List<WhitePostDto> getPosts(String sort, String cursor, int size, String userId) {
        Pageable pageable = PageRequest.of(0, size + 1); // 다음 페이지 확인을 위해 size + 1

        List<WhitePost> posts;

        switch (sort.toLowerCase()) {
            case "best":
                // 좋아요 내림차순 상위 3개 => cursor / size 무시
                posts = whitePostRepository.findTop3ByOrderByLikeCountDesc();
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

    private List<WhitePost> getPostsByLatest(String cursor, Pageable pageable) {
        if (cursor == null) {
            return whitePostRepository.findAllByOrderByPostIdDesc(pageable);
        } else {
            ObjectId cursorId = new ObjectId(cursor);
            return whitePostRepository.findByPostIdLessThanOrderByPostIdDesc(cursorId, pageable);
        }
    }

    private List<WhitePost> getPostsByLikesCount(String cursor, Pageable pageable) {
        if (cursor == null) {
            return whitePostRepository.findAllByOrderByLikeCountDescPostIdDesc(pageable);
        } else {
            String[] cursorParts = cursor.split("_");
            int likesCount = Integer.parseInt(cursorParts[0]);
            ObjectId postId = new ObjectId(cursorParts[1]);

            return whitePostRepository.findByLikeCountLessThanEqualAndPostIdLessThanOrderByLikeCountDescPostIdDesc(
                    likesCount, postId, pageable
            );
        }
    }

    public String getNextCursor(List<WhitePostDto> dtos, String sort) {
        if (dtos.isEmpty()) {
            return null;
        }

        if ("best".equalsIgnoreCase(sort)) {
            return null;
        }

        WhitePostDto lastDto = dtos.get(dtos.size() - 1);

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
        List<WhiteLike> likes = whiteLikeRepository.findByUserId(userId);
        return likes.stream()
                .map(WhiteLike::getPostId)
                .collect(Collectors.toSet());
    }

    private WhitePostDto convertToDto(WhitePost post, Set<ObjectId> likedPostIds) {
        WhitePostDto dto = new WhitePostDto();
        dto.setPostId(post.getPostId().toHexString());
        dto.setNickName(post.getNickName());
        dto.setContent(post.getContent());
        dto.setMethodNumber(post.getMethodNumber());
        dto.setLikeCount(post.getLikeCount());
        dto.setLikedByUser(likedPostIds.contains(post.getPostId()));
        // dto.setHotScore(post.getHotScore());
        return dto;
    }

    public void deleteWhitePost(HttpServletRequest request, HttpServletResponse response,
                                String accessToken, String stringPostId) {

        ObjectId userId = tokenServiceImpl.getUserIdFromAccessToken(request, response, accessToken);
        User existingUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserException(UserErrorResult.NOT_FOUND_USER));

        ObjectId postId;
        WhitePost whitePost;
        try {
            postId = new ObjectId(stringPostId);
            whitePost = whitePostRepository.findByPostId(postId)
                    .orElseThrow(() -> new PostException(PostErrorResult.NOT_FOUND_POST));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid post id: " + stringPostId);
        }

        if (!whitePost.getUserId().equals(userId)) {
            throw new PostException(PostErrorResult.UNAUTHORIZED_ACCESS);
        }

        whiteLikeService.removeAllWhiteLikesForPost(postId);
        whitePostRepository.delete(whitePost);
        deletePostLikesFromRedis(postId);
    }

    public void deletePostLikesFromRedis(ObjectId postId) {
        try {
            String key = "whitepost:" + postId + ":likes";
            redisTemplate.delete(key);
        } catch (Exception e) {
            // Log the error without interrupting the main flow
            System.err.println("Failed to delete likes from Redis for postId: " + postId);
            e.printStackTrace();
        }
    }

    public void saveWhitePost(HttpServletRequest request, HttpServletResponse response,
                              String accessToken, WhiteRequestDto whiteRequestDto) {

        ObjectId userId = tokenServiceImpl.getUserIdFromAccessToken(request, response, accessToken);
        User existingUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserException(UserErrorResult.NOT_FOUND_USER));

        WhitePost whitePost = createWhitePost(userId, whiteRequestDto);
        whitePostRepository.save(whitePost);
    }

    private WhitePost createWhitePost(ObjectId userId, WhiteRequestDto whiteRequestDto) {
        return WhitePost.builder()
                .postId(new ObjectId())
                .userId(userId)
                .nickName(whiteRequestDto.getNickName())
                .content(whiteRequestDto.getContent())
                .methodNumber(whiteRequestDto.getMethodNumber())
                .likeCount(0)
                .createdAt(LocalDateTime.now())
                .build();
    }
}