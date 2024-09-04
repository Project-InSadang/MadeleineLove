package sideproject.madeleinelove.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import sideproject.madeleinelove.entity.User;

public interface UserRepository extends MongoRepository<User,String> {

    User findByEmail(String email);
    User findByProviderId(String providerId);

}
