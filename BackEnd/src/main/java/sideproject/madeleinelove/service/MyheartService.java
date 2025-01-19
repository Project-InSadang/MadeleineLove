package sideproject.madeleinelove.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import sideproject.madeleinelove.dto.PostDTO;
import sideproject.madeleinelove.entity.User;
import sideproject.madeleinelove.exception.UserErrorResult;
import sideproject.madeleinelove.exception.UserException;
import sideproject.madeleinelove.model.Post;
import sideproject.madeleinelove.repository.*;

import java.util.Comparator;
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
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, Long> redisTemplate;

    public List<PostDTO> getPostsByUserId(HttpServletRequest request, HttpServletResponse response, String accessToken, boolean isWhite) {

        ObjectId userId = userService.getUserIdFromAccessToken(request, response, accessToken);
        User existingUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserException(UserErrorResult.NOT_FOUND_USER));

        List<? extends Post> posts = isWhite
                ? whitePostRepository.findByUserId(userId)
                : blackPostRepository.findByUserId(userId);

        return posts.stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .map(post -> convertToPostDTO(post, userId, isWhite))
                .collect(Collectors.toList());
    }

    private <T extends Post> PostDTO convertToPostDTO(T post, ObjectId userId, boolean isWhite) {

        String likedKey = getRedisKey(post.getPostId(), isWhite);
        boolean likedByUser = redisTemplate.opsForSet().isMember(likedKey, userId.toHexString());

        return new PostDTO(
                post.getPostId().toHexString(),
                likedByUser,
                post.getNickName(),
                post.getContent(),
                post.getMethodNumber(),
                post.getLikeCount(),
                post.getCreatedAt()
        );
    }

    private String getRedisKey(ObjectId postId, boolean isWhite) {
        return (isWhite ? "whitepost:" : "blackpost:") + postId + ":likes";
    }
}