package sideproject.madeleinelove.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import sideproject.madeleinelove.auth.*;
import sideproject.madeleinelove.repository.*;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService {

    private final TokenServiceImpl tokenServiceImpl;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    // 로그아웃 처리
    public void logout(HttpServletRequest request, HttpServletResponse response, String accessToken) {
        ObjectId userId = tokenServiceImpl.getUserIdFromAccessToken(request, response, accessToken);

        //JWT 토큰 처리
        handleTokenCleanup(accessToken, userId);
        handleCookieCleanup(response);
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