package sideproject.madeleinelove.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import sideproject.madeleinelove.entity.BlackPost;

import java.util.List;

public interface BlackPostRepository extends MongoRepository<BlackPost, ObjectId>, PostRepository {
    List<BlackPost> findByUserId(String userId);

    long countLikesByPostId(ObjectId postId);
}
