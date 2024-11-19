package sideproject.madeleinelove.service;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import sideproject.madeleinelove.repository.WhitePostRepository;
import sideproject.madeleinelove.entity.WhitePost;

@Service
public class WhitePostService {

    private final WhitePostRepository whitePostRepository;

    public WhitePostService(WhitePostRepository whitePostRepository) {
        this.whitePostRepository = whitePostRepository;
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
    }
}