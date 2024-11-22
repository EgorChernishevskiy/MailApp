package org.example.worker.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RedisLockService {

    private static final String LOCK_FORMAT = "%s::lock";

    ValueOperations<String, Long> valueOps;

    RedisTemplate<String, Long> redisTemplate;

    public boolean acquireLock(String key, Duration duration) {

        String lockKey = getLockKey(key);

        Long expiresAt = valueOps.get(lockKey);

        Long currentTime = System.currentTimeMillis();

        if (Objects.nonNull(expiresAt)) {

            if (currentTime <= expiresAt) {
                return false;
            }

            redisTemplate.delete(lockKey);
        }

        return Optional
                .ofNullable(valueOps.setIfAbsent(lockKey, currentTime + duration.toMillis()))
                .orElse(false);
    }

    public void releaseLock(String key) {

        String lockKey = getLockKey(key);

        redisTemplate.delete(lockKey);
    }

    private static String getLockKey(String key) {
        return String.format(LOCK_FORMAT, key);
    }
}
