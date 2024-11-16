package sideproject.madeleinelove.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sideproject.madeleinelove.entity.WhiteLike;
import sideproject.madeleinelove.entity.WhitePost;
import sideproject.madeleinelove.repository.WhiteLikeRepository;
import sideproject.madeleinelove.repository.WhitePostRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WhiteLikeService {

    private static final Logger logger = LoggerFactory.getLogger(WhiteLikeService.class);
    private final RedisTemplate<String, Integer> redisTemplate;

    @Autowired
    private WhiteLikeRepository whiteLikeRepository;

    @Autowired
    private final WhitePostRepository whitePostRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void likePost(String userId, ObjectId postId) {

        Query likeQuery = new Query(Criteria.where("userId").is(userId).and("postId").is(postId));
        WhiteLike existingLike = mongoTemplate.findOne(likeQuery, WhiteLike.class);

        if (existingLike == null) {
            redisTemplate.opsForValue().increment("whitepost:" + postId + ":likeCount");

            WhiteLike newLike = WhiteLike.builder()
                    .userId(userId)
                    .postId(postId)
                    .build();
            try {
                whiteLikeRepository.save(newLike);
                logger.info("User {} liked white post {}", userId, postId);
            } catch (DuplicateKeyException e) {
                logger.warn("User {} already liked white post {}", userId, postId);
            }
        }

        updateLikeCountInDB(postId);
    }

    public void unlikePost(String userId, ObjectId postId) {

        redisTemplate.opsForValue().decrement("whitepost:" + postId + ":likeCount");

        Query likeQuery = new Query(Criteria.where("userId").is(userId).and("postId").is(postId));
        WhiteLike existingLike = mongoTemplate.findOne(likeQuery, WhiteLike.class);
        if (existingLike != null) {
            whiteLikeRepository.delete(existingLike);
            logger.info("User {} unliked white post {}", userId, postId);
        }

        updateLikeCountInDB(postId);
    }

    public int getLikeCount(ObjectId postId) {
        Integer likeCount = redisTemplate.opsForValue().get("whitepost:" + postId + ":likeCount");
        return (likeCount != null) ? likeCount : 0; // 기본값 0
    }

    //주기적으로 모든 게시물의 좋아요 수를 MongoDB에서 조회한 후, Redis의 값을 업데이트
    //Redis의 데이터는 MongoDB의 최신 상태와 일치하도록 갱신
    @Scheduled(fixedRate = 180000) // 3분마다 Redis 캐시 업데이트
    public void refreshCache() {
        List<WhitePost> posts = whitePostRepository.findAll();
        for (WhitePost post : posts) {
            int likeCount = whiteLikeRepository.countByPostId(post.getPostId());
            redisTemplate.opsForValue().set("whitepost:" + post.getPostId() + ":likeCount", likeCount);
        }
    }

    //해당 게시물의 likeCount 필드를 업데이트,Redis의 값은 이미 업데이트된 상태이므로, 데이터 일관성을 유지
    private void updateLikeCountInDB(ObjectId postId) {
        try {
            int likeCount = whiteLikeRepository.countByPostId(postId);
            Query query = new Query(Criteria.where("_id").is(postId));
            Update update = new Update().set("likeCount", likeCount);
            UpdateResult result = mongoTemplate.updateFirst(query, update, WhitePost.class);

            if (result.getMatchedCount() == 0) {
                System.out.println("No white document found with id: " + postId);
            } else {
                System.out.println("Updated likeCount for white postId: " + postId);
            }
        } catch (Exception e) {
            System.out.println("Error updating white like count: " + e.getMessage());
        }
    }
}
