package sideproject.madeleinelove.service;

import org.bson.types.ObjectId;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import sideproject.madeleinelove.repository.WhitePostRepository;
import sideproject.madeleinelove.entity.WhitePost;

@Service
public class WhitePostService {

    private final WhitePostRepository whitePostRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public WhitePostService(WhitePostRepository whitePostRepository, RedisTemplate<String, String> redisTemplate) {
        this.whitePostRepository = whitePostRepository;
        this.redisTemplate = redisTemplate;
    }

    public void deleteWhitePost(String postId, String userId) {
        // Validate and convert postId to ObjectId
        ObjectId objectId;
        try {
            objectId = new ObjectId(postId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid post id: " + postId);
        }

        WhitePost whitePost = whitePostRepository.findById(objectId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + objectId));
        if (!whitePost.getUserId().equals(userId)) {
            throw new IllegalArgumentException("User is not authorized to delete this post");
        }
        whitePostRepository.delete(whitePost);

        // Delete the corresponding likes from Redis
        deletePostLikesFromRedis(postId);
    }

    private void deletePostLikesFromRedis(String postId) {
        try {
            String key = "whitepost:" + postId + ":likes";
            redisTemplate.delete(key);
        } catch (Exception e) {
            // Log the error without interrupting the main flow
            System.err.println("Failed to delete likes from Redis for postId: " + postId);
            e.printStackTrace();
        }
    }

}