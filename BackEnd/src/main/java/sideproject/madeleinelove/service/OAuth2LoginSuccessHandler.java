package sideproject.madeleinelove.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import sideproject.madeleinelove.auth.CookieUtil;
import sideproject.madeleinelove.auth.GoogleUserInfo;
import sideproject.madeleinelove.auth.JwtUtil;
import sideproject.madeleinelove.auth.OAuth2UserInfo;
import sideproject.madeleinelove.entity.RefreshToken;
import sideproject.madeleinelove.entity.User;
import sideproject.madeleinelove.repository.RefreshTokenRepository;
import sideproject.madeleinelove.repository.UserRepository;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final CookieUtil cookieUtil;
    @Value("${jwt.redirect}")
    private String REDIRECT_URI;

    @Value("${jwt.access-token.expiration-time}")
    private Long ACCESS_TOKEN_EXPIRATION_TIME;

    @Value("${jwt.refresh-token.expiration-time}")
    private Long REFRESH_TOKEN_EXPIRATION_TIME;

    private OAuth2UserInfo oAuth2UserInfo;

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        final String provider = token.getAuthorizedClientRegistrationId();

        // 구글 로그인 요청
        if (provider.equals("google")) {
            log.info("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(token.getPrincipal().getAttributes());
        }

        // 정보 추출
        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();

        User user = userRepository.findByProviderId(providerId);

        if (user == null) {
            // 신규 유저인 경우
            log.info("신규 유저입니다. 유저 등록을 진행합니다.");
            user = User.builder()
                    .userId(new ObjectId())
                    .email(email)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(user);
        }

        // 액세스 토큰 발급
        String accessToken = jwtUtil.generateAccessToken(user.getUserId(), ACCESS_TOKEN_EXPIRATION_TIME);

        // 리프레쉬 토큰 발급 후 저장
        String refreshToken = jwtUtil.generateRefreshToken(user.getUserId(), REFRESH_TOKEN_EXPIRATION_TIME);
        RefreshToken newRefreshToken = RefreshToken.builder()
                .userId(user.getUserId())
                .token(refreshToken)
                .build();
        refreshTokenRepository.save(newRefreshToken);

        // 액세스 토큰과 리프레쉬 토큰을 HttpOnly 쿠키에 설정
        ResponseCookie accessTokenCookie = cookieUtil.createCookie("access_token", accessToken, ACCESS_TOKEN_EXPIRATION_TIME);
        ResponseCookie refreshTokenCookie = cookieUtil.createCookie("refresh_token", refreshToken, REFRESH_TOKEN_EXPIRATION_TIME);
        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        // 리다이렉트 처리
        getRedirectStrategy().sendRedirect(request, response, REDIRECT_URI);

        log.info("PROVIDER : {}", provider);
        log.info("PROVIDER_ID : {}", providerId);
    }

}
