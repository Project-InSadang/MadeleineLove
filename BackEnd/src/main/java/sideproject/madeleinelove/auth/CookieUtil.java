package sideproject.madeleinelove.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieUtil {
    private final JwtUtil jwtUtil;
    private final String FRESH_TOKEN_COOKIE = "refresh_token";
    private final String ACCESS_TOKEN_COOKIE = "access_token";

    // 액세스 토큰이 담긴 쿠키를 생성하는 메서드
    public Cookie createAccessTokenCookie(String accessToken, long expirationMillis) {
        Cookie cookie = new Cookie(ACCESS_TOKEN_COOKIE, accessToken);
        // 쿠키 속성 설정
        cookie.setHttpOnly(true); // JavaScript 접근 방지
        cookie.setSecure(true);   // HTTPS에서만 전송
        cookie.setPath("/");      // 모든 경로에서 쿠키 접근 가능
        cookie.setMaxAge((int) expirationMillis); // 쿠키 만료시간 설정
        return cookie;
    }

    // 리프레쉬 토큰이 담긴 쿠키를 생성하는 메서드
    public Cookie createFreshTokenCookie(ObjectId userId, long expirationMillis) {
        String cookieValue = jwtUtil.generateRefreshToken(userId, expirationMillis);
        Cookie cookie = new Cookie(FRESH_TOKEN_COOKIE, cookieValue);
        // 쿠키 속성 설정
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) expirationMillis);
        return cookie;
    }

    // 특정 쿠키를 찾아 반환하는 메서드
    public Cookie getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie;
                }
            }
        }
        return null;
    }
}