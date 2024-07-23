package sideproject.madeleinelove.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import sideproject.madeleinelove.entity.User;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    // 추가적인 데이터베이스 작업 메서드 정의
    User findByEmail(String email);
}