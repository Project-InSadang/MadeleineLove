package sideproject.madeleinelove.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;
import sideproject.madeleinelove.entity.RefreshToken;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken,String> {

    RefreshToken findByUserId(ObjectId userId);
    @Transactional
    RefreshToken deleteByUserId(ObjectId userId);

}
