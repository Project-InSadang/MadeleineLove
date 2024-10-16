package sideproject.madeleinelove.service;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import sideproject.madeleinelove.entity.Likes;
import sideproject.madeleinelove.entity.WhitePost;
import sideproject.madeleinelove.repository.LikeRepository;
import sideproject.madeleinelove.repository.WhitePostRepository;

@Service
public class LikeService {

    @Autowired
    private WhitePostRepository whitePostRepository;
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void toggleLike(String userId, ObjectId postId) {
        if (!likeRepository.existsByUserIdAndPostId(userId, postId)) {
            incrementLikeCount(postId);

            Likes newLike = Likes.builder()
                    .userId(userId)
                    .postId(postId)
                    .build();
            likeRepository.save(newLike);
        } else {
            decrementLikeCount(postId);

            Query likeQuery = new Query(Criteria.where("userId").is(userId).and("postId").is(postId));
            Likes existingLike = mongoTemplate.findOne(likeQuery, Likes.class);
            if (existingLike != null) {
                likeRepository.delete(existingLike);
            }
        }
    }

    private void incrementLikeCount(ObjectId postId) {
        Query query = new Query(Criteria.where("_id").is(postId));
        Update update = new Update().inc("likeCount", 1);
        mongoTemplate.updateFirst(query, update, WhitePost.class);
    }

    private void decrementLikeCount(ObjectId postId) {
        Query query = new Query(Criteria.where("_id").is(postId));
        Update update = new Update().inc("likeCount", -1);
        mongoTemplate.updateFirst(query, update, WhitePost.class);
    }
}
