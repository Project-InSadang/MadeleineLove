package sideproject.madeleinelove.service;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import sideproject.madeleinelove.dto.PostDTO;
import sideproject.madeleinelove.model.Post;
import sideproject.madeleinelove.repository.BlackLikeRepository;
import sideproject.madeleinelove.repository.BlackPostRepository;
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

    @Autowired
    private BlackPostRepository blackPostRepository;
    @Autowired
    private BlackLikeRepository blackLikeRepository;

    @Autowired
    private RedisTemplate<String, Long> redisTemplate;

    public List<PostDTO> getPostsByUserId(String userId, boolean isWhite) {
        List<? extends Post> posts = isWhite
                ? whitePostRepository.findByUserId(userId)
                : blackPostRepository.findByUserId(userId);

        return posts.stream()
                .map(post -> convertToPostDTO(post, userId, isWhite))
                .collect(Collectors.toList());
    }

    private <T extends Post> PostDTO convertToPostDTO(T post, String userId, boolean isWhite) {

        String likedKey = getRedisKey(post.getPostId(), isWhite);
        boolean likedByUser = redisTemplate.opsForSet().isMember(likedKey, userId);

        return new PostDTO(
                post.getNickName(),
                post.getContent(),
                post.getMethodNumber(),
                post.getLikeCount(),
                likedByUser,
                post.getPostId().toHexString()
        );
    }

    private String getRedisKey(ObjectId postId, boolean isWhite) {
        return (isWhite ? "whitepost:" : "blackpost:") + postId + ":likes";
    }
}