package sideproject.madeleinelove.repository;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import sideproject.madeleinelove.entity.WhitePost;

import java.util.List;

public interface WhitePostRepository extends MongoRepository<WhitePost, ObjectId> {

    // 최신순 조회 (ObjectId를 사용하여 내림차순 정렬)
    List<WhitePost> findAllByOrderByPostIdDesc(Pageable pageable);

    // 커서 이후의 게시물 조회 (최신순)
    List<WhitePost> findByPostIdLessThanOrderByPostIdDesc(ObjectId postId, Pageable pageable);

    // 추천순 조회 (`likesCount` 내림차순)
    List<WhitePost> findAllByOrderByLikesCountDescPostIdDesc(Pageable pageable);

    // 커서 이후의 게시물 조회 (추천순)
    List<WhitePost> findByLikesCountLessThanOrLikesCountEqualsAndPostIdLessThanOrderByLikesCountDescPostIdDesc(
            Integer likesCount, ObjectId postId, Pageable pageable
    );


    /*
    추천순 조회 (Hot Score 내림차순)
    List<WhitePost> findAllByOrderByHotScoreDescPostIdDesc(Pageable pageable);

    커서 이후의 게시물 조회 (추천순)
    List<WhitePost> findByHotScoreLessThanOrHotScoreEqualsAndPostIdLessThanOrderByHotScoreDescPostIdDesc(
            Double hotScore, Double equalHotScore, ObjectId postId, Pageable pageable
    );
     */

}
