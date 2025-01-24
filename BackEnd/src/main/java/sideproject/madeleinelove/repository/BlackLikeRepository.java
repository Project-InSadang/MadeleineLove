package sideproject.madeleinelove.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import sideproject.madeleinelove.dto.UserIdDTO;
import sideproject.madeleinelove.entity.BlackLike;

import java.util.List;

public interface BlackLikeRepository extends MongoRepository<BlackLike, ObjectId> {

    int countByPostId(ObjectId postId);

    @Query(value = "{ 'postId' : ?0 }", fields = "{ 'userId' : 1 }")
    List<UserIdDTO> findByPostId(ObjectId postId);

    @Transactional
    void deleteAllByUserId(ObjectId userId);

}