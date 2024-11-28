package sideproject.madeleinelove.service;

import org.bson.types.ObjectId;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import sideproject.madeleinelove.repository.BlackPostRepository;
import sideproject.madeleinelove.entity.BlackPost;

@Service
public class BlackPostService {

    private final BlackPostRepository blackPostRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public BlackPostService(BlackPostRepository blackPostRepository, RedisTemplate<String, String> redisTemplate) {
        this.blackPostRepository = blackPostRepository;
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

        BlackPost blackPost = blackPostRepository.findById(objectId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + objectId));
        if (!blackPost.getUserId().equals(userId)) {
            throw new IllegalArgumentException("User is not authorized to delete this post");
        }
        blackPostRepository.delete(blackPost);

        // Delete the corresponding likes from Redis
        deletePostLikesFromRedis(postId);
    }

private void deletePostLikesFromRedis(String postId) {
    try {
        String key = "blackpost:" + postId + ":likes";
        redisTemplate.delete(key);
    } catch (Exception e) {
        // Log the error without interrupting the main flow
        System.err.println("Failed to delete likes from Redis for postId: " + postId);
        e.printStackTrace();
    }
}
}