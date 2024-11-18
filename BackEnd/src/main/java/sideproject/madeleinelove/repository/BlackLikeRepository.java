package sideproject.madeleinelove.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import sideproject.madeleinelove.dto.UserIdDTO;
import sideproject.madeleinelove.entity.BlackLike;
import sideproject.madeleinelove.entity.WhiteLike;

import java.util.List;
import java.util.Optional;

public interface BlackLikeRepository extends MongoRepository<BlackLike, ObjectId> {

    int countByPostId(ObjectId postId);

    @Query(value = "{ 'postId' : ?0 }", fields = "{ 'userId' : 1 }")
    List<UserIdDTO> findByPostId(ObjectId postId);

}