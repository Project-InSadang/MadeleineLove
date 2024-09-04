package sideproject.madeleinelove.auth;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sideproject.madeleinelove.entity.User;
import sideproject.madeleinelove.repository.UserRepository;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private final UserRepository userRepository;
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public JwtUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 액세스 토큰을 발급하는 메서드
    public String generateAccessToken(ObjectId userId, long expirationMillis) {
        log.info("액세스 토큰이 발행되었습니다.");
        return Jwts
                .builder()
                .claim("userId", userId.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(this.getSigningKey())
                .compact();
    }

    // 레지스터 토큰을 발급하는 메서드
    public String generateRegisterToken(String email, String provider, String providerId, long expirationMillis) {
        log.info("레지스트 토큰이 발행되었습니다.");
        return Jwts
                .builder()
                .claim("email", email)
                .claim("provider", provider)
                .claim("providerId", providerId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(this.getSigningKey())
                .compact();
    }

    // 리프레쉬 토큰을 발급하는 메서드
    public String generateRefreshToken(ObjectId userId, long expirationMillis) {
        log.info("리프레쉬 토큰이 발행되었습니다.");
        return Jwts
                .builder()
                .claim("userId", userId.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(this.getSigningKey())
                .compact();
    }

    // 응답 헤더에서 액세스 토큰을 반환하는 메서드
    public String getTokenFromHeader(String authorizationHeader) {
        return authorizationHeader.substring(7);
    }

    // 토큰에서 userId를 반환하는 메서드
    public String getUserIdFromToken(String token) {
        try {
            String userId = Jwts
                    .parser()
                    .verifyWith(this.getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("userId", String.class);
            log.info("userId를 반환합니다.");
            return userId;
        } catch (JwtException | IllegalArgumentException e) {
            // 토큰이 유효하지 않은 경우
            log.warn("유효하지 않은 토큰: userId 반환 실패");
            return null;
        }
    }

    // 토큰에서 provideId를 반환하는 메서드
    public String getProviderIdFromToken(String token) {
        try {
            String provideId = Jwts
                    .parser()
                    .verifyWith(this.getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("providerId", String.class);
            log.info("provideId를 반환합니다.");
            return provideId;
        } catch (JwtException | IllegalArgumentException e) {
            // 토큰이 유효하지 않은 경우
            log.warn("유효하지 않은 토큰: provideId 반환 실패");
            return null;
        }
    }

    // 토큰에서 유저를 반환하는 메서드
    public User getUserFromHeader(String authorizationHeader) {
        String token = getTokenFromHeader(authorizationHeader);
        String providerId = getProviderIdFromToken(token);
        return userRepository
                .findByProviderId(providerId);
    }

    // JWT 토큰의 유효 기간을 확인하는 메서드
    public boolean isTokenExpired(String token) {
        try {
            Date expirationDate = Jwts
                    .parser()
                    .verifyWith(this.getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();
            log.info("토큰의 유효 기간을 확인합니다.");
            return expirationDate.before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            // 토큰이 유효하지 않은 경우
            log.warn("유효하지 않은 토큰: 유효 기간 확인 실패");
            return false;
        }
    }

}
