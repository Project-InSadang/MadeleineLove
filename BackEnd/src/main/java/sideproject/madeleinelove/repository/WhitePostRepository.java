package sideproject.madeleinelove.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import sideproject.madeleinelove.entity.WhitePost;

import java.util.List;

public interface WhitePostRepository extends MongoRepository<WhitePost, ObjectId>, PostRepository {
    List<WhitePost> findByUserId(String userId);
}