package sideproject.madeleinelove.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import sideproject.madeleinelove.entity.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    User findByProviderId(String providerId);

    @Query("{ 'userId': ?0 }")
    Optional<User> findByUserId(ObjectId userId);
}