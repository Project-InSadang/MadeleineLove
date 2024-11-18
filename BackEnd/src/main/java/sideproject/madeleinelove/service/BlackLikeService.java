package sideproject.madeleinelove.service;

import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mapping.MappingException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sideproject.madeleinelove.dto.PostIdDTO;
import sideproject.madeleinelove.dto.UserIdDTO;
import sideproject.madeleinelove.entity.BlackLike;
import sideproject.madeleinelove.entity.BlackPost;
import sideproject.madeleinelove.repository.BlackLikeRepository;
import sideproject.madeleinelove.repository.BlackPostRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BlackLikeService {
    private static final Logger logger = LoggerFactory.getLogger(BlackLikeService.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BlackLikeRepository blackLikeRepository;

    @Autowired
    private BlackPostRepository blackPostRepository;

    private String getRedisKey(ObjectId postId) {
        return "blackpost:" + postId + ":likes";
    }

    @Transactional
    public void addLike(ObjectId postId, String userId) {
        //Redis
        String key = getRedisKey(postId);
        redisTemplate.opsForSet().add(key, userId);

        //MongoDB
        if (!isLiked(postId, userId)) {
            BlackLike newLike = BlackLike.builder()
                    .userId(userId)
                    .postId(postId)
                    .build();
            try {
                blackLikeRepository.save(newLike);
                logger.info("User {} liked black post {}", userId, postId);
                updateLikeCountInDB(postId);
            } catch (DuplicateKeyException e) {
                logger.warn("User {} already liked black post {}", userId, postId);
            }
        }
    }

    public boolean isLiked(ObjectId postId, String userId) {
        Query likeQuery = new Query(Criteria.where("userId").is(userId).and("postId").is(postId));
        BlackLike existingLike = mongoTemplate.findOne(likeQuery, BlackLike.class);
        return existingLike != null;
    }

    @Transactional
    public void removeLike(ObjectId postId, String userId) {
        //Redis
        String key = getRedisKey(postId);
        redisTemplate.opsForSet().remove(key, userId);

        //MongoDB
        BlackLike existingLike = findLike(postId, userId);
        if (existingLike != null) {
            blackLikeRepository.delete(existingLike);
            logger.info("User {} unliked black post {}", userId, postId);
        }
        updateLikeCountInDB(postId);
    }

    private BlackLike findLike(ObjectId postId, String userId) {
        Query likeQuery = new Query(Criteria.where("userId").is(userId).and("postId").is(postId));
        return mongoTemplate.findOne(likeQuery, BlackLike.class);
    }

    @Scheduled(fixedRate = 180000)
    public void checkConsistency() {
        try {
            List<PostIdDTO> postIdDTOs = blackPostRepository.findAllPostIds();

            for (PostIdDTO postIdDTO : postIdDTOs) {
                ObjectId postId = postIdDTO.getPostId();
                String key = getRedisKey(postId);
                long redisLikeCount = redisTemplate.opsForSet().size(key);
                long dbLikeCount = blackLikeRepository.countByPostId(postId);

                if (redisLikeCount != dbLikeCount) {
                    synchronizeLikes(postId, redisLikeCount, dbLikeCount, key);
                }
            }
        } catch (MappingException e) {
            logger.error("Mapping error occurred: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error in checkConsistency: {}", e.getMessage(), e);
        }
    }

    private void synchronizeLikes(ObjectId postId, long redisLikeCount, long dbLikeCount, String key) {
        try {
            List<UserIdDTO> userIdDTOs = blackLikeRepository.findByPostId(postId);
            Set<String> dbUserIds = userIdDTOs.stream()
                    .map(UserIdDTO::getUserId)
                    .collect(Collectors.toSet());

            Set<String> redisUserIds = getRedisUserIds(key);

            if (dbLikeCount > redisLikeCount) {
                Set<String> idsToAdd = new HashSet<>(dbUserIds);
                idsToAdd.removeAll(redisUserIds);
                addIdsToRedis(key, idsToAdd);
            } else {
                Set<String> idsToRemove = new HashSet<>(redisUserIds);
                idsToRemove.removeAll(dbUserIds);
                removeIdsFromRedis(key, idsToRemove);
            }
        } catch (Exception e) {
            logger.error("Error in synchronizing likes for black postId: {}. Error: {}", postId, e.getMessage(), e);
        }
    }

    private Set<String> getRedisUserIds(String key) {
        Set<String> redisUserIds = redisTemplate.opsForSet().members(key);
        return redisUserIds != null ? redisUserIds : new HashSet<>();
    }

    private void addIdsToRedis(String key, Set<String> idsToAdd) {
        if (!idsToAdd.isEmpty()) {
            redisTemplate.opsForSet().add(key, idsToAdd.toArray(String[]::new));
            logger.info("Added {} IDs to Redis for key: {}", idsToAdd.size(), key);
        }
    }

    private void removeIdsFromRedis(String key, Set<String> idsToRemove) {
        if (!idsToRemove.isEmpty()) {
            redisTemplate.opsForSet().remove(key, (Object[]) idsToRemove.toArray(new String[0]));
            logger.info("Removed {} IDs from Redis for key: {}", idsToRemove.size(), key);
        }
    }

    private void updateLikeCountInDB(ObjectId postId) {
        try {
            int likeCount = blackLikeRepository.countByPostId(postId);
            Query query = new Query(Criteria.where("_id").is(postId));
            Update update = new Update().set("likeCount", likeCount);
            UpdateResult result = mongoTemplate.updateFirst(query, update, BlackPost.class);

            if (result.getMatchedCount() == 0) {
                logger.warn("No black post found with id: {}", postId);
            } else {
                logger.info("Updated likeCount for black postId: {} to {}", postId, likeCount);
            }
        } catch (Exception e) {
            logger.error("Error updating likeCount for black postId {}: {}", postId, e.getMessage(), e);
        }
    }
}
