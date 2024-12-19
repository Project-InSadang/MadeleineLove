package sideproject.madeleinelove.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import sideproject.madeleinelove.dto.PostIdDTO;
import sideproject.madeleinelove.entity.BlackPost;

import java.util.List;

public interface BlackPostRepository extends MongoRepository<BlackPost, ObjectId> {
    List<BlackPost> findByUserId(String userId);

    List<ObjectId> findPostIdsByUserId(String userId);

    void deleteByUserId(String userId);

    @Query(value = "{}", fields = "{ 'postId' : 1 }")
    List<PostIdDTO> findAllPostIds();

}