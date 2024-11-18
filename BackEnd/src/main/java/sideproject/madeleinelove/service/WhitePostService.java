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
        WhitePost whitePost = whitePostRepository.findById(new ObjectId(postId))
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        if (!whitePost.getUserId().equals(userId)) {
            throw new IllegalArgumentException("User is not authorized to delete this post");
        }
        whitePostRepository.delete(whitePost);
    }
}