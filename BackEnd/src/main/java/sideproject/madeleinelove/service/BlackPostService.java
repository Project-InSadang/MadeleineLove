package sideproject.madeleinelove.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import sideproject.madeleinelove.dto.BlackPostDto;
import sideproject.madeleinelove.entity.User;
import sideproject.madeleinelove.exception.PostErrorResult;
import sideproject.madeleinelove.exception.PostException;
import sideproject.madeleinelove.exception.UserErrorResult;
import sideproject.madeleinelove.exception.UserException;
import sideproject.madeleinelove.repository.BlackPostRepository;
import sideproject.madeleinelove.entity.BlackPost;
import sideproject.madeleinelove.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BlackPostService {

    @Autowired
    private BlackPostRepository blackPostRepository;
    private final TokenServiceImpl tokenServiceImpl;
    private final UserRepository userRepository;
    private final BlackLikeService blackLikeService;
    private final RedisTemplate<String, String> redisTemplate;

    /*public BlackPost save(String userId, BlackPostDto blackPostDto) {
        // DTO to Entity
        BlackPost blackPost = new BlackPost();
        blackPost.setPostId(new ObjectId());
        blackPost.setUserId(userId);

        String nickName = blackPostDto.getNickName();
        if (nickName == null || nickName.trim().isEmpty()) {
            nickName = "레니";
        }
        blackPost.setNickName(nickName);

        blackPost.setContent(blackPostDto.getContent());
        blackPost.setMethodNumber(blackPostDto.getMethodNumber());
        blackPost.setLikeCount(0);

        return blackPostRepository.save(blackPost);
    }
     */

    public BlackPost saveBlackPost(HttpServletRequest request, HttpServletResponse response,
                                   String accessToken, BlackPostDto blackRequestDto) {

        ObjectId userId = tokenServiceImpl.getUserIdFromAccessToken(request, response, accessToken);
        User existingUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserException(UserErrorResult.NOT_FOUND_USER));

        BlackPost blackPost = createBlackPost(userId, blackRequestDto);
        return blackPostRepository.save(blackPost);
    }

    public void deleteBlackPost(HttpServletRequest request, HttpServletResponse response,
                                String accessToken, String stringPostId) {

        ObjectId userId = tokenServiceImpl.getUserIdFromAccessToken(request, response, accessToken);
        User existingUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserException(UserErrorResult.NOT_FOUND_USER));

        ObjectId postId;
        BlackPost blackPost;
        try {
            postId = new ObjectId(stringPostId);
            blackPost = blackPostRepository.findByPostId(postId)
                    .orElseThrow(() -> new PostException(PostErrorResult.NOT_FOUND_POST));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid post id: " + stringPostId);
        }

        if (!blackPost.getUserId().equals(userId)) {
            throw new PostException(PostErrorResult.UNAUTHORIZED_ACCESS);
        }

        blackLikeService.removeAllBlackLikesForPost(postId);
        blackPostRepository.delete(blackPost);
        deletePostLikesFromRedis(postId);
    }

    public void deletePostLikesFromRedis(ObjectId postId) {
        try {
            String key = "blackpost:" + postId + ":likes";
            redisTemplate.delete(key);
        } catch (Exception e) {
            // Log the error without interrupting the main flow
            System.err.println("Failed to delete likes from Redis for postId: " + postId);
            e.printStackTrace();
        }
    }

    private BlackPost createBlackPost(ObjectId userId, BlackPostDto blackRequestDto) {
        return BlackPost.builder()
                .postId(new ObjectId())
                .userId(userId)
                .nickName(blackRequestDto.getNickName())
                .content(blackRequestDto.getContent())
                .methodNumber(blackRequestDto.getMethodNumber())
                .likeCount(0)
                .createdAt(LocalDateTime.now())
                .build();
    }

}
