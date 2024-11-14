package sideproject.madeleinelove.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import sideproject.madeleinelove.entity.WhiteLike;

import java.util.List;
import java.util.Optional;

public interface WhiteLikeRepository extends MongoRepository<WhiteLike, ObjectId> {
    List<WhiteLike> findByUserId(String userId);

    int countByPostId(ObjectId postId);

    boolean existsByUserIdAndPostId(String userId, ObjectId postId);

    Optional<WhiteLike> findByUserIdAndPostId(String userId, ObjectId postId);
}
