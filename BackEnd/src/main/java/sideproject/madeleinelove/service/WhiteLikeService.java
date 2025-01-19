package sideproject.madeleinelove.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import com.mongodb.client.result.UpdateResult;;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
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
import sideproject.madeleinelove.entity.User;
import sideproject.madeleinelove.entity.WhiteLike;
import sideproject.madeleinelove.entity.WhitePost;
import sideproject.madeleinelove.exception.PostErrorResult;
import sideproject.madeleinelove.exception.PostException;
import sideproject.madeleinelove.exception.UserErrorResult;
import sideproject.madeleinelove.exception.UserException;
import sideproject.madeleinelove.repository.UserRepository;
import sideproject.madeleinelove.repository.WhiteLikeRepository;
import sideproject.madeleinelove.repository.WhitePostRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WhiteLikeService {

    private static final Logger logger = LoggerFactory.getLogger(WhiteLikeService.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    private final WhiteLikeRepository whiteLikeRepository;
    private final WhitePostRepository whitePostRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final TokenServiceImpl tokenServiceImpl;

    private String getRedisKey(ObjectId postId) {
        return "whitepost:" + postId + ":likes";
    }

    @Transactional
    public void addLike(HttpServletRequest request, HttpServletResponse response, String accessToken, String stringPostId) {

        //userId 인증
        ObjectId userId = userService.getUserIdFromAccessToken(request, response, accessToken);
        User existingUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserException(UserErrorResult.NOT_FOUND_USER));

        //postId 인증
        ObjectId postId = new ObjectId(stringPostId);
        WhitePost existingPost = whitePostRepository.findByPostId(postId)
                .orElseThrow(() -> new PostException(PostErrorResult.NOT_FOUND_POST));

        //Redis
        String key = getRedisKey(postId);
        redisTemplate.opsForSet().add(key, userId.toHexString());

        //MongoDB
        if (!isLiked(postId, userId)) {
            WhiteLike newLike = WhiteLike.builder()
                    .userId(userId)
                    .postId(postId)
                    .build();
            try {
                whiteLikeRepository.save(newLike);
                logger.info("User {} liked white post {}", userId, postId);
                updateLikeCountInDB(postId);
            } catch (DuplicateKeyException e) {
                logger.warn("User {} already liked white post {}", userId, postId);
            }
        }else {
            throw new PostException(PostErrorResult.ALREADY_LIKED);
        }
    }

    public boolean isLiked(ObjectId postId, ObjectId userId) {
        Query likeQuery = new Query(Criteria.where("userId").is(userId).and("postId").is(postId));
        WhiteLike existingLike = mongoTemplate.findOne(likeQuery, WhiteLike.class);
        return existingLike != null;
    }

    @Transactional
    public void removeLike(HttpServletRequest request, HttpServletResponse response, String accessToken, String stringPostId) {

        ObjectId userId = userService.getUserIdFromAccessToken(request, response, accessToken);
        User existingUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserException(UserErrorResult.NOT_FOUND_USER));

        ObjectId postId = new ObjectId(stringPostId);
        WhitePost existingPost = whitePostRepository.findByPostId(postId)
                .orElseThrow(() -> new PostException(PostErrorResult.NOT_FOUND_POST));

        //Redis
        String key = getRedisKey(postId);
        redisTemplate.opsForSet().remove(key, userId.toHexString());

        //MongoDB
        WhiteLike existingLike = findLike(postId, userId);
        if (existingLike != null) {
            whiteLikeRepository.delete(existingLike);
            logger.info("User {} unliked white post {}", userId, postId);
            updateLikeCountInDB(postId);
        }else {
            throw new PostException(PostErrorResult.ALREADY_UNLIKED);
        }
    }

    private WhiteLike findLike(ObjectId postId, ObjectId userId) {
        Query likeQuery = new Query(Criteria.where("userId").is(userId).and("postId").is(postId));
        return mongoTemplate.findOne(likeQuery, WhiteLike.class);
    }

    @Scheduled(fixedRate = 180000) // 3분마다 Redis 캐시 업데이트
    public void checkConsistency() {
        try {
            List<PostIdDTO> postIdDTOs = whitePostRepository.findAllPostIds();

            for (PostIdDTO postIdDTO : postIdDTOs) {
                ObjectId postId = postIdDTO.getPostId();
                String key = getRedisKey(postId);
                long redisLikeCount = redisTemplate.opsForSet().size(key);
                long dbLikeCount = whiteLikeRepository.countByPostId(postId);

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

    //MongoDB Like 테이블과 Redis Set의 일관성
    private void synchronizeLikes(ObjectId postId, long redisLikeCount, long dbLikeCount, String key) {
        try {
            List<UserIdDTO> userIdDTOs = whiteLikeRepository.findByPostId(postId);
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
            logger.error("Error in synchronizing likes for white postId: {}. Error: {}", postId, e.getMessage(), e);
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

    //MongoDB Like 테이블과 Post 테이블의 likeCount 일관성
    private void updateLikeCountInDB(ObjectId postId) {
        try {
            int likeCount = whiteLikeRepository.countByPostId(postId);
            Query query = new Query(Criteria.where("_id").is(postId));
            Update update = new Update().set("likeCount", likeCount);
            UpdateResult result = mongoTemplate.updateFirst(query, update, WhitePost.class);

            if (result.getMatchedCount() == 0) {
                logger.warn("No white post found with id: {}", postId);
            } else {
                logger.info("Updated likeCount for white postId: {} to {}", postId, likeCount);
            }
        } catch (Exception e) {
            logger.error("Error updating likeCount for white postId {}: {}", postId, e.getMessage(), e);
        }
    }

    @Transactional
    public void removeAllWhiteLikesForPost(ObjectId postId) {

        String key = getRedisKey(postId);
        Set<String> userIds = getRedisUserIds(key);

        for (String userId : userIds) {
            ObjectId objectUserId = new ObjectId(userId);
            WhiteLike existingLike = findLike(postId, objectUserId);
            if (existingLike != null) {
                whiteLikeRepository.delete(existingLike);
                logger.info("Removed white like from mongoDB for postID {}, userId {}", postId, userId);
            }
        }
    }
}
