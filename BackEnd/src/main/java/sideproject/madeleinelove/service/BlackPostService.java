package sideproject.madeleinelove.service;

import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sideproject.madeleinelove.dto.BlackPostDto;
import sideproject.madeleinelove.entity.Like;
import sideproject.madeleinelove.entity.BlackPost;
import sideproject.madeleinelove.repository.LikeRepository;
import sideproject.madeleinelove.repository.BlackPostRepository;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BlackPostService {

    private final BlackPostRepository blackPostRepository;
    private final LikeRepository likeRepository;

    public BlackPostService(BlackPostRepository blackPostRepository, LikeRepository likeRepository) {
        this.blackPostRepository = blackPostRepository;
        this.likeRepository = likeRepository;
    }

    public List<BlackPostDto> getPosts(String sort, String cursor, int size, String userId) {
        Pageable pageable = PageRequest.of(0, size + 1); // 다음 페이지 확인을 위해 size + 1

        List<BlackPost> posts;
        if ("recommended".equalsIgnoreCase(sort)) {
            posts = getPostsByLikesCount(cursor, pageable);
            // posts = getPostsByHotScore(cursor, pageable);
        } else {
            posts = getPostsByLatest(cursor, pageable);
        }

        boolean hasNext = posts.size() > size;
        if (hasNext) {
            posts = posts.subList(0, size);
        }

        // 사용자 좋아요 정보 가져오기
        Set<ObjectId> likedPostIds = getUserLikedPostIds(userId);

        // DTO 변환 및 isLiked 설정
        List<BlackPostDto> dtos = posts.stream()
                .map(post -> convertToDto(post, likedPostIds))
                .collect(Collectors.toList());

        return dtos;
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
        List<Like> likes = likeRepository.findByUserId(userId);
        return likes.stream()
                .map(Like::getPostId)
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
    
}
