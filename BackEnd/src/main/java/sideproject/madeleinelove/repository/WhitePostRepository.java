package sideproject.madeleinelove.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import sideproject.madeleinelove.entity.WhitePost;

public interface WhitePostRepository extends MongoRepository<WhitePost, Integer> {

}