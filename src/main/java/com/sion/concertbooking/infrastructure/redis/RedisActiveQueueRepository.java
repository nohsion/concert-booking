package com.sion.concertbooking.infrastructure.redis;

import com.sion.concertbooking.domain.activequeue.ActiveQueue;
import com.sion.concertbooking.domain.activequeue.ActiveQueueRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Repository
public class RedisActiveQueueRepository implements ActiveQueueRepository {

    private static final String REDIS_CONCERT_ACTIVE_TOKENS_KEY_FORMAT = "CONCERT_%d:ACTIVE_TOKENS";
    private static final String REDIS_ACTIVE_CONCERTS_KEY = "ACTIVE_CONCERTS";

    private final RedisTemplate<String, String> redisTemplate;

    public RedisActiveQueueRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String save(ActiveQueue activeQueue) {
        String redisKey = String.format(REDIS_CONCERT_ACTIVE_TOKENS_KEY_FORMAT, activeQueue.getConcertId());

        String tokenId = activeQueue.getTokenId();
        LocalDateTime expiredAt = activeQueue.getExpiredAt();
        long expiredMillis = DateTimeUtils.toEpochMillis(expiredAt);

        redisTemplate.opsForZSet().add(redisKey, tokenId, expiredMillis);

        return tokenId;
    }

    @Override
    public Long findRank(String tokenId, long concertId) {
        String redisKey = String.format(REDIS_CONCERT_ACTIVE_TOKENS_KEY_FORMAT, concertId);
        return redisTemplate.opsForZSet().rank(redisKey, tokenId);
    }

    @Override
    public Long deleteExpiredTokens(long concertId, LocalDateTime now) {
        String redisKey = String.format(REDIS_CONCERT_ACTIVE_TOKENS_KEY_FORMAT, concertId);

        long currentMillis = DateTimeUtils.toEpochMillis(now);
        return redisTemplate.opsForZSet().removeRangeByScore(redisKey, 0, currentMillis);
    }

    @Override
    public List<String> getActiveTokens(final long concertId) {
        String redisKey = String.format(REDIS_CONCERT_ACTIVE_TOKENS_KEY_FORMAT, concertId);
        Set<String> activeTokenSet = redisTemplate.opsForZSet().range(redisKey, 0, -1);
        if (CollectionUtils.isEmpty(activeTokenSet)) {
            return Collections.emptyList();
        }
        return List.copyOf(activeTokenSet);
    }

    @Override
    public List<Long> getActiveConcerts() {
        Set<String> concertIds = redisTemplate.opsForSet().members(REDIS_ACTIVE_CONCERTS_KEY);
        if (CollectionUtils.isEmpty(concertIds)) {
            return Collections.emptyList();
        }
        return concertIds.stream()
                .map(Long::parseLong)
                .toList();
    }

    @Override
    public void addActiveConcert(long concertId) {
        redisTemplate.opsForSet().add(REDIS_ACTIVE_CONCERTS_KEY, String.valueOf(concertId));
    }

    @Override
    public void removeActiveConcert(long concertId) {
        redisTemplate.opsForSet().remove(REDIS_ACTIVE_CONCERTS_KEY, String.valueOf(concertId));
    }


}
