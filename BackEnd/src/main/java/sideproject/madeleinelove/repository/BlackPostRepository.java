package sideproject.madeleinelove.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import sideproject.madeleinelove.entity.BlackPost;

import java.util.List;

public interface BlackPostRepository extends MongoRepository<BlackPost, ObjectId> {

    // 좋아요 개수 내림차순 기준으로 상위 3개 게시물 조회
    List<BlackPost> findTop3ByOrderByLikeCountDesc();

}
