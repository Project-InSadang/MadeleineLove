package sideproject.madeleinelove.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import sideproject.madeleinelove.domain.BlackPost;

public interface BlackPostRepository extends MongoRepository<BlackPost, Integer> {
}
