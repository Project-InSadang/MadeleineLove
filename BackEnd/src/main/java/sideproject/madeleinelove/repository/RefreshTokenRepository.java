package sideproject.madeleinelove.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import sideproject.madeleinelove.entity.RefreshToken;

@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, Long> {
    @Query("{ 'userId': ?0 }")
    RefreshToken findByUserId(ObjectId userId);

    @Query(value = "{ 'userId': ?0 }", delete = true)
    void deleteByUserId(ObjectId userId);
}