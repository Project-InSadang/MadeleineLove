package sideproject.madeleinelove.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import sideproject.madeleinelove.dto.PostIdDTO;
import sideproject.madeleinelove.entity.BlackPost;

import java.util.List;
import java.util.Optional;

public interface BlackPostRepository extends MongoRepository<BlackPost, ObjectId> {
    List<BlackPost> findAllByUserId(ObjectId userId);

    @Query("{ 'postId': ?0 }")
    Optional<BlackPost> findByPostId(ObjectId postId);

    @Query(value = "{}", fields = "{ 'postId' : 1 }")
    List<PostIdDTO> findAllPostIds();

}