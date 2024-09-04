package sideproject.madeleinelove.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import sideproject.madeleinelove.auth.CookieUtil;
import sideproject.madeleinelove.auth.JwtUtil;
import sideproject.madeleinelove.dto.TokenDto;
import sideproject.madeleinelove.entity.RefreshToken;
import sideproject.madeleinelove.exception.TokenErrorResult;
import sideproject.madeleinelove.exception.TokenException;
import sideproject.madeleinelove.repository.RefreshTokenRepository;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    @Value("${jwt.access-token.expiration-time}")
    private long ACCESS_TOKEN_EXPIRATION_TIME;

    @Value("${jwt.refresh-token.expiration-time}")
    private long REFRESH_TOKEN_EXPIRATION_TIME;

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    @Override
    public TokenDto.TokenResponse reissueAccessToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = cookieUtil.getCookie("refresh_token", request);
        String refreshToken = cookie.getValue();
        String userId = jwtUtil.getUserIdFromToken(refreshToken);
        RefreshToken existRefreshToken = refreshTokenRepository.findByUserId(new ObjectId(userId));
        String newAccessToken;

        if (!existRefreshToken.getToken().equals(refreshToken) || jwtUtil.isTokenExpired(refreshToken)) {
            // 리프레쉬 토큰이 다르거나 만료된 경우
            // 401 에러를 던져 재로그인 요청
            throw new TokenException(TokenErrorResult.INVALID_REFRESH_TOKEN);
        } else {
            // 엑세스 토큰 재발급
            newAccessToken = jwtUtil.generateAccessToken(new ObjectId(userId), ACCESS_TOKEN_EXPIRATION_TIME);
        }

        // 리프레쉬 토큰이 담긴 쿠키 생성 후 설정
        ResponseCookie newCookie = cookieUtil.createCookie("refresh_token", userId, REFRESH_TOKEN_EXPIRATION_TIME);
        response.addHeader("Set-Cookie", newCookie.toString());

        return TokenDto.TokenResponse.of(newAccessToken);
    }

}
