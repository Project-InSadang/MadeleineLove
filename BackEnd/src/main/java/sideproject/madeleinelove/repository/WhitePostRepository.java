package sideproject.madeleinelove.repository;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import sideproject.madeleinelove.dto.PostIdDTO;
import sideproject.madeleinelove.entity.WhitePost;

import java.util.List;
import java.util.Optional;

public interface WhitePostRepository extends MongoRepository<WhitePost, ObjectId> {

    List<WhitePost> findByUserId(ObjectId userId);

    @Query("{ 'postId': ?0 }")
    Optional<WhitePost> findByPostId(ObjectId postId);

    void deleteByUserId(String userId);

    @Query(value = "{}", fields = "{ 'postId' : 1 }")
    List<PostIdDTO> findAllPostIds();

    // 최신순 조회 (ObjectId를 사용하여 내림차순 정렬)
    List<WhitePost> findAllByOrderByPostIdDesc(Pageable pageable);

    // 커서 이후의 게시물 조회 (최신순)
    List<WhitePost> findByPostIdLessThanOrderByPostIdDesc(ObjectId postId, Pageable pageable);

    // 추천순 조회 (`likesCount` 내림차순)
    List<WhitePost> findAllByOrderByLikeCountDescPostIdDesc(Pageable pageable);

    // 커서 이후의 게시물 조회 (추천순)
    List<WhitePost> findByLikeCountLessThanEqualAndPostIdLessThanOrderByLikeCountDescPostIdDesc(
            Integer likesCount, ObjectId postId, Pageable pageable
    );

}