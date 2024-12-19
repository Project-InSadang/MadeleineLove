package sideproject.madeleinelove.service;

import org.bson.types.ObjectId;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import sideproject.madeleinelove.auth.CookieUtil;
import sideproject.madeleinelove.auth.JwtUtil;
import sideproject.madeleinelove.entity.*;
import sideproject.madeleinelove.exception.TokenErrorResult;
import sideproject.madeleinelove.exception.TokenException;
import sideproject.madeleinelove.repository.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;
    private final WhitePostRepository whitePostRepository;
    private final BlackPostRepository blackPostRepository;
    private final WhiteLikeRepository whiteLikeRepository;
    private final BlackLikeRepository blackLikeRepository;
    private final BlackLikeService blackLikeService;
    private final WhiteLikeService whiteLikeService;

    @Value("${kakao.unlink}")
    private String KAUTH_UNLINK_URL_HOST;
    @Value("${kakao.service.app.key}")
    private String KAKAO_SERVICE_APP_KEY;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Transactional
    public void withdraw(String authorizationHeader) {

        String accessToken = jwtUtil.getTokenFromHeader(authorizationHeader);

        if (jwtUtil.isTokenExpired(accessToken)) {
            throw new TokenException(TokenErrorResult.INVALID_ACCESS_TOKEN);
        }

        String userIdString = jwtUtil.getUserIdFromToken(accessToken);
        ObjectId userId = new ObjectId(userIdString);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));


        // 유저가 누른 좋아요
        removeAllPostsLikesForUser(userIdString);
        // 유저의 게시물에 눌린 좋아요 삭제
        blackLikeService.removeAllBlackLikesForPost(userId);

        blackPostRepository.deleteByUserId(userIdString);
        whitePostRepository.deleteByUserId(userIdString);

        refreshTokenRepository.deleteByUserId(userId);
        userRepository.delete(user);

        disconnectSocialAccount(user.getProvider(), KAKAO_SERVICE_APP_KEY);
        logger.info("User {} has been withdrawn successfully.", userIdString);
    }

    private void disconnectSocialAccount(String provider, String adminKey) {
        if ("kakao".equals(provider)) {
            String url = KAUTH_UNLINK_URL_HOST;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK" + adminKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new RuntimeException("카카오 계정 연결 끊기 실패: " + response.getBody());
            }
        }
    }

    private void removeAllPostsLikesForUser(String userId) {
        List<BlackPost> blackPosts = blackPostRepository.findByUserId(userId);
        for (BlackPost post : blackPosts) {
            blackLikeService.removeAllBlackLikesForPost(post.getPostId());
        }

        List<WhitePost> whitePosts = whitePostRepository.findByUserId(userId);
        for (WhitePost post : whitePosts) {
            whiteLikeService.removeAllWhiteLikesForPost(post.getPostId());
        }
    }

    private void removeAllLikesByUser(String userId) {
        List<BlackLike> blackLikes = blackLikeRepository.findByUserId(userId);
        List<WhiteLike> whiteLikes = whiteLikeRepository.findByUserId(userId);
        for (BlackLike bLike : blackLikes) {
            blackLikeService.removeLike(bLike.getPostId(), userId);
        }

        for (WhiteLike wLike : whiteLikes) {
            blackLikeService.removeLike(wLike.getPostId(), userId);
        }

    }
}