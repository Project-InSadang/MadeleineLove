package sideproject.madeleinelove.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sideproject.madeleinelove.auth.CookieUtil;
import sideproject.madeleinelove.auth.JwtUtil;
import sideproject.madeleinelove.dto.TokenDTO;
import sideproject.madeleinelove.entity.RefreshToken;
import sideproject.madeleinelove.exception.TokenErrorResult;
import sideproject.madeleinelove.exception.TokenException;
import sideproject.madeleinelove.repository.RefreshTokenRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    @Value("${jwt.access-token.expiration-time}")
    private long ACCESS_TOKEN_EXPIRATION_TIME;

    @Value("${jwt.refresh-token.expiration-time}")
    private long REFRESH_TOKEN_EXPIRATION_TIME;

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    private static final Logger logger = LoggerFactory.getLogger(TokenServiceImpl.class);

    @Override
    public String reissueAccessToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = cookieUtil.getCookie(request);
        String refreshToken = cookie.getValue();

        String userIdString = jwtUtil.getUserIdFromToken(refreshToken);
        ObjectId userId = new ObjectId(userIdString);
        RefreshToken existRefreshToken = refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new TokenException(TokenErrorResult.REFRESH_TOKEN_NOT_FOUND));

        if (!existRefreshToken.getRefreshToken().equals(refreshToken) || jwtUtil.isTokenExpired(refreshToken)) {
            throw new TokenException(TokenErrorResult.INVALID_REFRESH_TOKEN);
        }

        String newAccessToken = jwtUtil.generateAccessToken(userId, ACCESS_TOKEN_EXPIRATION_TIME);

        return newAccessToken;
    }

    @Override
    public TokenDTO.TokenResponse validateAccessToken(HttpServletRequest request, HttpServletResponse response, String authorizationHeader) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new TokenException(TokenErrorResult.INVALID_TOKEN);
        }
        String accessToken = authorizationHeader.replace("Bearer ", "");

        if (jwtUtil.isTokenExpired(accessToken)) {
            logger.info("Access token expired for token: {}", accessToken);
            String newAccessToken = reissueAccessToken(request, response);
            return new TokenDTO.TokenResponse(true, newAccessToken);
        }

        return new TokenDTO.TokenResponse(false, accessToken);
    }
}