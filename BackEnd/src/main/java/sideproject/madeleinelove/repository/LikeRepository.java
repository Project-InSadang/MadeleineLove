package sideproject.madeleinelove.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import sideproject.madeleinelove.entity.Like;

import java.util.List;

public interface LikeRepository extends MongoRepository<Like, ObjectId> {

    List<Like> findByUserId(String userId);

}
