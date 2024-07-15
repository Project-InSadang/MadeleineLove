package sideproject.madeleinelove.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import sideproject.madeleinelove.entity.BlackPost;

public interface BlackPostRepository extends MongoRepository<BlackPost, ObjectId> {
}
