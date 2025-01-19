package sideproject.madeleinelove.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.bson.types.ObjectId;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private String getRedisKey(ObjectId userId) {
        return "REFRESH:" + userId;
    }

    public void save(final RefreshToken refreshToken) {

        String key = getRedisKey(refreshToken.getUserId());
        redisTemplate.opsForValue().set(key, refreshToken.getRefreshToken());
        redisTemplate.expire(key, REFRESH_TOKEN_EXPIRATION_TIME, TimeUnit.MILLISECONDS);
    }

    public Optional<RefreshToken> findByUserId(final ObjectId userId) {

        String key = getRedisKey(userId);
        String refreshToken = redisTemplate.opsForValue().get(key);

        if (Objects.isNull(refreshToken)) {
            return Optional.empty();
        }

        return Optional.of(new RefreshToken(userId, refreshToken));
    }

    public void deleteByUserId(ObjectId userId) {

        String key = getRedisKey(userId);
        redisTemplate.delete(key);
    }
}