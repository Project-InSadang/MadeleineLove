package sideproject.madeleinelove.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.bson.types.ObjectId;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import sideproject.madeleinelove.entity.RefreshToken;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    @Value("${jwt.refresh-token.expiration-time}")
    private long REFRESH_TOKEN_EXPIRATION_TIME;

    private final RedisTemplate redisTemplate;

    public void save(final RefreshToken refreshToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        String refreshTokenKey = "REFRESH:" + refreshToken.getUserId().toString();
        valueOperations.set(refreshTokenKey, refreshToken.getRefreshToken());

        redisTemplate.expire(refreshTokenKey, REFRESH_TOKEN_EXPIRATION_TIME, TimeUnit.MILLISECONDS);
    }

    public Optional<RefreshToken> findByUserId(final ObjectId userId) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        String refreshTokenKey = "REFRESH:" + userId.toString();
        String refreshToken = valueOperations.get(refreshTokenKey);

        if (Objects.isNull(refreshToken)) {
            return Optional.empty();
        }

        return Optional.of(new RefreshToken(userId, refreshToken));
    }
}