package sideproject.madeleinelove.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sideproject.madeleinelove.auth.CookieUtil;
import sideproject.madeleinelove.auth.JwtUtil;
import sideproject.madeleinelove.repository.RefreshTokenRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final CookieUtil cookieUtil;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    // 쿠키를 삭제하는 메서드
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // 1) 쿠키 가져오기
        Cookie refreshTokenCookie = cookieUtil.getCookie(request);
        if (refreshTokenCookie != null) {
            String refreshToken = refreshTokenCookie.getValue();

            // 2) refresh token에서 userId 파싱
            String userIdString = jwtUtil.getUserIdFromToken(refreshToken);
            ObjectId userId = new ObjectId(userIdString);

            // 3) Redis에서 해당 userId의 refresh token 삭제
            refreshTokenRepository.deleteByUserId(userId);
        }

        // 4) 쿠키 자체도 바로 만료시켜 삭제
        Cookie cookie = cookieUtil.deleteCookie();
        response.addCookie(cookie);
    }

}
