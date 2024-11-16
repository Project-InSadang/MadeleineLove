package sideproject.madeleinelove.service;

import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sideproject.madeleinelove.entity.BlackLike;
import sideproject.madeleinelove.entity.BlackPost;
import sideproject.madeleinelove.repository.BlackLikeRepository;
import sideproject.madeleinelove.repository.BlackPostRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlackLikeService {
    private static final Logger logger = LoggerFactory.getLogger(WhiteLikeService.class);
    private final RedisTemplate<String, Integer> redisTemplate;

    @Autowired
    private BlackLikeRepository blackLikeRepository;

    @Autowired
    private final BlackPostRepository blackPostRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void likePost(String userId, ObjectId postId) {

        Query likeQuery = new Query(Criteria.where("userId").is(userId).and("postId").is(postId));
        BlackLike existingLike = mongoTemplate.findOne(likeQuery, BlackLike.class);

        if (existingLike == null) {
            redisTemplate.opsForValue().increment("blackpost:" + postId + ":likeCount");

            BlackLike newLike = BlackLike.builder()
                    .userId(userId)
                    .postId(postId)
                    .build();
            try {
                blackLikeRepository.save(newLike);
                logger.info("User {} liked black post {}", userId, postId);
            } catch (DuplicateKeyException e) {
                logger.warn("User {} already liked black post {}", userId, postId);
            }
        }

        updateLikeCountInDB(postId);
    }

    public void unlikePost(String userId, ObjectId postId) {

        redisTemplate.opsForValue().decrement("blackpost:" + postId + ":likeCount");

        Query likeQuery = new Query(Criteria.where("userId").is(userId).and("postId").is(postId));
        BlackLike existingLike = mongoTemplate.findOne(likeQuery, BlackLike.class);
        if (existingLike != null) {
            blackLikeRepository.delete(existingLike);
            logger.info("User {} unliked black post {}", userId, postId);
        }

        updateLikeCountInDB(postId);
    }

    public int getLikeCount(ObjectId postId) {
        Integer likeCount = redisTemplate.opsForValue().get("blackpost:" + postId + ":likeCount");
        return (likeCount != null) ? likeCount : 0;
    }

    @Scheduled(fixedRate = 180000)
    public void refreshCache() {
        List<BlackPost> posts = blackPostRepository.findAll();
        for (BlackPost post : posts) {
            int likeCount = blackLikeRepository.countByPostId(post.getPostId());
            redisTemplate.opsForValue().set("blackpost:" + post.getPostId() + ":likeCount", likeCount);
        }
    }

    private void updateLikeCountInDB(ObjectId postId) {
        try {
            int likeCount = blackLikeRepository.countByPostId(postId);
            Query query = new Query(Criteria.where("_id").is(postId));
            Update update = new Update().set("likeCount", likeCount);
            UpdateResult result = mongoTemplate.updateFirst(query, update, BlackPost.class);

            if (result.getMatchedCount() == 0) {
                System.out.println("No black document found with id: " + postId);
            } else {
                System.out.println("Updated likeCount for black postId: " + postId);
            }
        } catch (Exception e) {
            System.out.println("Error updating black like count: " + e.getMessage());
        }
    }
}
