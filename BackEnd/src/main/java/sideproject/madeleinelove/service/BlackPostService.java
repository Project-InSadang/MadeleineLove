package sideproject.madeleinelove.service;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import sideproject.madeleinelove.repository.BlackPostRepository;
import sideproject.madeleinelove.entity.BlackPost;

@Service
public class BlackPostService {

    private final BlackPostRepository blackPostRepository;

    public BlackPostService(BlackPostRepository blackPostRepository) {
        this.blackPostRepository = blackPostRepository;
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
    }
}