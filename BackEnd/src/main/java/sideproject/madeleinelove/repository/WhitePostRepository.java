package sideproject.madeleinelove.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import sideproject.madeleinelove.dto.PostIdDTO;
import sideproject.madeleinelove.entity.WhitePost;

import java.util.List;

public interface WhitePostRepository extends MongoRepository<WhitePost, ObjectId>, PostRepository {
    List<WhitePost> findByUserId(String userId);

    @Query(value = "{}", fields = "{ 'postId' : 1 }")
    List<PostIdDTO> findAllPostIds();
}