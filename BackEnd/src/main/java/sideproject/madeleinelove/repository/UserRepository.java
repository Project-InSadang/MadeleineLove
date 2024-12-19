package sideproject.madeleinelove.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import sideproject.madeleinelove.entity.User;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByProviderId(String providerId);

}