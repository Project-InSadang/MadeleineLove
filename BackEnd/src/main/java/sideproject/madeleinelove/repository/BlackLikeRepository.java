package sideproject.madeleinelove.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import sideproject.madeleinelove.entity.BlackLike;

import java.util.List;
import java.util.Optional;

public interface BlackLikeRepository extends MongoRepository<BlackLike, ObjectId> {
    List<BlackLike> findByUserId(String userId);

    int countByPostId(ObjectId postId);

    boolean existsByUserIdAndPostId(String userId, ObjectId postId);

    Optional<BlackLike> findByUserIdAndPostId(String userId, ObjectId postId);

}
