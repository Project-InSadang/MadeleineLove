package sideproject.madeleinelove.service;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
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

    // 좋아요 개수 기준 상위 3개 게시물 조회
    public List<BlackPost> getTop3PostsByLikeCount() {
        return blackPostRepository.findTop3ByOrderByLikeCountDesc();
    }

    // 특정 사용자가 좋아요를 누른 게시물의 ID 집합을 가져오는 메서드
    public Set<ObjectId> getUserLikedPostIds(String userId) {
        if (userId == null) {
            return Collections.emptySet();
        }
        List<Like> likes = likeRepository.findByUserId(userId);
        return likes.stream()
                .map(Like::getPostId)
                .collect(Collectors.toSet());
    }

}
