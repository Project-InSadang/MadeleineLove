package sideproject.madeleinelove.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sideproject.madeleinelove.entity.RefreshToken;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, ObjectId> {
    @Query("{ 'userId': ?0 }")
    Optional<RefreshToken> findByUserId(ObjectId userId);

    @Transactional
    @Query(value = "{ 'userId': ?0 }", delete = true)
    void deleteByUserId(ObjectId userId);
}