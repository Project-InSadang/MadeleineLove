package sideproject.madeleinelove.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sideproject.madeleinelove.dto.WhitePostDTO;
import sideproject.madeleinelove.entity.WhitePost;
import sideproject.madeleinelove.repository.WhiteLikeRepository;
import sideproject.madeleinelove.repository.WhitePostRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyheartService {

    @Autowired
    private WhitePostRepository whitePostRepository;
    @Autowired
    private WhiteLikeRepository whiteLikeRepository;

    public List<WhitePostDTO> getWhitePostsByUserId(String userId) {

        List<WhitePost> whitePosts = whitePostRepository.findByUserId(userId);

        return whitePosts.stream()
                .map(post -> {
                    boolean likedByUser = whiteLikeRepository.findByUserIdAndPostId(userId, post.getPostId()).isPresent();

                    return new WhitePostDTO(
                            post.getNickName(),
                            post.getContent(),
                            post.getFillMethod(),
                            post.getLikeCount(),
                            likedByUser,
                            post.getPostId().toHexString()
                    );
                })
                .collect(Collectors.toList());
        
    }
}
