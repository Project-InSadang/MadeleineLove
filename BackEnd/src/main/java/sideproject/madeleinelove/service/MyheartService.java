package sideproject.madeleinelove.service;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sideproject.madeleinelove.dto.WhitePostDTO;
import sideproject.madeleinelove.entity.Likes;
import sideproject.madeleinelove.entity.WhitePost;
import sideproject.madeleinelove.repository.LikeRepository;
import sideproject.madeleinelove.repository.WhitePostRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyheartService {

    @Autowired
    private WhitePostRepository whitePostRepository;
    @Autowired
    private LikeRepository likeRepository;

    public List<WhitePostDTO> getWhitePostsByUserId(String userId) {
        List<WhitePost> whitePosts = whitePostRepository.findByUserId(userId);

        List<ObjectId> likedPostIds = likeRepository.findByUserId(userId).stream()
                .map(Likes::getPostId)
                .collect(Collectors.toList());

        return whitePosts.stream()
                .map(post -> new WhitePostDTO(
                        post.getNickName(),
                        post.getContent(),
                        post.getFillMethod(),
                        post.getLikeCount(),
                        likedPostIds.contains(post.getPostId())
                ))
                .collect(Collectors.toList());
    }
}
