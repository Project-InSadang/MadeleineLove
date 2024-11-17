package sideproject.madeleinelove.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import sideproject.madeleinelove.entity.Likes;

import java.util.List;

public interface LikeRepository extends MongoRepository<Likes, ObjectId> {
    List<Likes> findByUserId(String userId);
    boolean existsByUserIdAndPostId(String userId, ObjectId postId);
}
