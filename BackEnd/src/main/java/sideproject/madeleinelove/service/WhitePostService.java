package sideproject.madeleinelove.service;

import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sideproject.madeleinelove.dto.WhitePostDto;
import sideproject.madeleinelove.entity.Like;
import sideproject.madeleinelove.entity.WhitePost;
import sideproject.madeleinelove.repository.LikeRepository;
import sideproject.madeleinelove.repository.WhitePostRepository;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WhitePostService {

    private final WhitePostRepository whitePostRepository;
    private final LikeRepository likeRepository;

    public WhitePostService(WhitePostRepository whitePostRepository, LikeRepository likeRepository) {
        this.whitePostRepository = whitePostRepository;
        this.likeRepository = likeRepository;
    }

    public List<WhitePostDto> getPosts(String sort, String cursor, int size, String userId) {
        Pageable pageable = PageRequest.of(0, size + 1); // 다음 페이지 확인을 위해 size + 1

        List<WhitePost> posts;
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
        List<WhitePostDto> dtos = posts.stream()
                .map(post -> convertToDto(post, likedPostIds))
                .collect(Collectors.toList());

        return dtos;
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

    /*
    private List<WhitePost> getPostsByHotScore(String cursor, Pageable pageable) {
        if (cursor == null) {
            return whitePostRepository.findAllByOrderByHotScoreDescPostIdDesc(pageable);
        } else {
            String[] cursorParts = cursor.split("_");
            double hotScore = Double.parseDouble(cursorParts[0]);
            ObjectId postId = new ObjectId(cursorParts[1]);

            return whitePostRepository.findByHotScoreLessThanOrHotScoreEqualsAndPostIdLessThanOrderByHotScoreDescPostIdDesc(
                    hotScore, hotScore, postId, pageable
            );
        }
    }
     */

    public String getNextCursor(List<WhitePostDto> dtos, String sort) {
        if (dtos.isEmpty()) {
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
        List<Like> likes = likeRepository.findByUserId(userId);
        return likes.stream()
                .map(Like::getPostId)
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

    /*
    Hot Score 계산 메서드
    public void calculateHotScoreForPosts(List<WhitePost> posts) {
        for (WhitePost post : posts) {
            double hotScore = calculateHotScore(post);
            post.setHotScore(hotScore);
            whitePostRepository.save(post);
        }
    }

    private double calculateHotScore(WhitePost post) {
        int likes = post.getLikesCount();
        long postTime = post.getPostId().getTimestamp();
        long currentTime = System.currentTimeMillis() / 1000L;

        long ageInSeconds = currentTime - postTime;
        if (ageInSeconds <= 0) {
            ageInSeconds = 1;
        }

        return likes / Math.pow(ageInSeconds, 1.5);
    }
     */

}
