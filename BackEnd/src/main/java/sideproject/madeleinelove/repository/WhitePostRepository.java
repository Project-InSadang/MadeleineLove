package sideproject.madeleinelove.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import sideproject.madeleinelove.entity.WhitePost;

public interface WhitePostRepository extends MongoRepository<WhitePost, Integer> {
    // 추가적인 데이터베이스 작업 메서드 정의
}