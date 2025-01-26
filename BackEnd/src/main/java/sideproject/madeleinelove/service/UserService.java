package sideproject.madeleinelove.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sideproject.madeleinelove.auth.*;
import sideproject.madeleinelove.entity.*;
import sideproject.madeleinelove.exception.UserErrorResult;
import sideproject.madeleinelove.exception.UserException;
import sideproject.madeleinelove.repository.*;
import sideproject.madeleinelove.token.auth.OAuth2UnlinkService;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final TokenServiceImpl tokenServiceImpl;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final WhiteLikeService whiteLikeService;
    private final BlackLikeService blackLikeService;
    private final WhitePostService whitePostService;
    private final BlackPostService blackPostService;
    private final WhitePostRepository whitePostRepository;
    private final BlackPostRepository blackPostRepository;
    private final WhiteLikeRepository whiteLikeRepository;
    private final BlackLikeRepository blackLikeRepository;
    private final OAuth2UnlinkService oAuth2UnlinkService;
    private final RedisService redisService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private void unlinkOAuthAccount(User user) {
        String provider = user.getProvider();
        String providerId = user.getProviderId();

        oAuth2UnlinkService.unlink(provider, providerId); // 소셜 계정 연동 해제
        redisService.deleteValues(provider + ":" + providerId); // OAuth2 액세스 토큰 삭제
    }

    @Transactional
    public void withdraw(HttpServletRequest request, HttpServletResponse response, String accessToken) {

        ObjectId userId = tokenServiceImpl.getUserIdFromAccessToken(request, response, accessToken);
        User existingUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserException(UserErrorResult.NOT_FOUND_USER));

        //Oauth 토큰 처리
        unlinkOAuthAccount(existingUser);

        //사용자 게시물 및 좋아요 삭제
        deleteUserPostsAndLikes(userId);

        //JWT 토큰 처리
        handleTokenCleanup(accessToken, userId);
        handleCookieCleanup(response);

        //사용자 삭제
        userRepository.delete(existingUser);
    }

    private void deleteUserPostsAndLikes(ObjectId userId) {
        // 게시물 삭제
        List<WhitePost> whitePosts = whitePostRepository.findAllByUserId(userId);
        List<BlackPost> blackPosts = blackPostRepository.findAllByUserId(userId);

        for (WhitePost post : whitePosts) {
            whiteLikeService.removeAllWhiteLikesForPost(post.getPostId());
            whitePostService.deletePostLikesFromRedis(post.getPostId());
            whitePostRepository.delete(post);
        }

        for (BlackPost post : blackPosts) {
            blackLikeService.removeAllBlackLikesForPost(post.getPostId());
            blackPostService.deletePostLikesFromRedis(post.getPostId());
            blackPostRepository.delete(post);
        }

        // 좋아요 삭제
        whiteLikeRepository.deleteAllByUserId(userId);
        blackLikeRepository.deleteAllByUserId(userId);
    }

    private void handleTokenCleanup(String accessToken, ObjectId userId) {
        refreshTokenRepository.deleteByUserId(userId); // 리프레쉬 토큰 삭제
        Date expirationDate = jwtUtil.getExpirationDateFromToken(accessToken);
        tokenServiceImpl.addToBlacklist(accessToken, expirationDate); // 액세스 토큰 블랙리스트 등록
    }

    private void  handleCookieCleanup(HttpServletResponse response) {
        ResponseCookie responseCookie = ResponseCookie.from("refresh_token", null)
                .maxAge(0)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .build();

        response.addHeader("Set-Cookie", responseCookie.toString());
    }
}
