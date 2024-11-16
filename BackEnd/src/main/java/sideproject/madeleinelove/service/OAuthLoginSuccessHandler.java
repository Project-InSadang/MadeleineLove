package sideproject.madeleinelove.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import sideproject.madeleinelove.auth.*;
import sideproject.madeleinelove.entity.RefreshToken;
import sideproject.madeleinelove.entity.User;
import sideproject.madeleinelove.repository.RefreshTokenRepository;
import sideproject.madeleinelove.repository.UserRepository;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${jwt.redirect}")
    private String REDIRECT_URI;

    @Value("${jwt.access-token.expiration-time}")
    private long ACCESS_TOKEN_EXPIRATION_TIME;

    @Value("${jwt.refresh-token.expiration-time}")
    private long REFRESH_TOKEN_EXPIRATION_TIME;

    private OAuth2UserInfo oAuth2UserInfo = null;

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        final String provider = token.getAuthorizedClientRegistrationId();

        switch (provider) {
            /*
            case "google" -> {
                log.info("구글 로그인 요청");
                oAuth2UserInfo = new GoogleUserInfo(token.getPrincipal().getAttributes());
            }*/
            case "kakao" -> {
                log.info("카카오 로그인 요청");
                oAuth2UserInfo = new KakaoUserInfo(token.getPrincipal().getAttributes());
            }
            case "naver" -> {
                log.info("네이버 로그인 요청");
                oAuth2UserInfo = new NaverUserInfo((Map<String, Object>) token.getPrincipal().getAttributes().get("response"));
            }
        }

        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();

        User existUser = userRepository.findByProviderId(providerId);
        User user;

        if (existUser == null) {
            log.info("신규 유저입니다. 등록을 진행합니다.");

            user = User.builder()
                    .email(email)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(user);
        } else {
            log.info("기존 유저입니다.");

            refreshTokenRepository.deleteByUserId(existUser.getUserId());
            user = existUser;
            user.update();
            userRepository.save(user);
        }

        // 리프레쉬 토큰이 담긴 쿠키 생성 후 설정
        ResponseCookie cookie = cookieUtil.createCookie(user.getUserId(), REFRESH_TOKEN_EXPIRATION_TIME);
        response.addHeader("Set-Cookie", cookie.toString());

        // 새로운 리프레쉬 토큰 DB 저장
        RefreshToken newRefreshToken = RefreshToken.builder()
                .userId(user.getUserId())
                .refreshToken(cookie.getValue())
                .build();
        refreshTokenRepository.save(newRefreshToken);

        // 액세스 토큰 발급
        String accessToken = jwtUtil.generateAccessToken(user.getUserId(), ACCESS_TOKEN_EXPIRATION_TIME);

        String redirectUri = String.format(REDIRECT_URI, accessToken);
        getRedirectStrategy().sendRedirect(request, response, redirectUri);

        log.info("유저 이메일 : {}", email);
        log.info("PROVIDER : {}", provider);
        log.info("PROVIDER_ID : {}", providerId);
    }
}