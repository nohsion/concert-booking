package com.sion.concertbooking.infrastructure.redis;

import com.sion.concertbooking.domain.watingqueue.WaitingQueue;
import com.sion.concertbooking.domain.watingqueue.WaitingQueueRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Repository
public class RedisWaitingQueueRepository implements WaitingQueueRepository {

    private static final String REDIS_CONCERT_WAITING_TOKENS_KEY_FORMAT = "CONCERT_%d:WAITING_TOKENS";
    private static final String REDIS_CONCERT_WAITING_CONCERTS_KEY = "WAITING_CONCERTS";

    private final RedisTemplate<String, String> redisTemplate;

    public RedisWaitingQueueRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String save(WaitingQueue waitingQueue) {
        String redisKey = String.format(REDIS_CONCERT_WAITING_TOKENS_KEY_FORMAT, waitingQueue.getConcertId());

        String tokenId = waitingQueue.getTokenId();
        LocalDateTime createdAt = waitingQueue.getCreatedAt();
        long currentMillis = DateTimeUtils.toEpochMillis(createdAt);

        redisTemplate.opsForZSet().add(redisKey, tokenId, currentMillis);

        return tokenId;
    }

    @Override
    public Long findRank(String tokenId, long concertId) {
        String redisKey = String.format(REDIS_CONCERT_WAITING_TOKENS_KEY_FORMAT, concertId);
        return redisTemplate.opsForZSet().rank(redisKey, tokenId);
    }

    @Override
    public void popMin(int count, long concertId) {
        String redisKey = String.format(REDIS_CONCERT_WAITING_TOKENS_KEY_FORMAT, concertId);
        redisTemplate.opsForZSet().popMin(redisKey, count);
    }

    @Override
    public List<String> getWaitingTokens(long concertId) {
        String redisKey = String.format(REDIS_CONCERT_WAITING_TOKENS_KEY_FORMAT, concertId);
        Set<String> waitingTokenSet = redisTemplate.opsForZSet().range(redisKey, 0, -1);
        if (CollectionUtils.isEmpty(waitingTokenSet)) {
            return Collections.emptyList();
        }
        return List.copyOf(waitingTokenSet);
    }

    @Override
    public void removeToken(final String tokenId, final long concertId) {
        String redisKey = String.format(REDIS_CONCERT_WAITING_TOKENS_KEY_FORMAT, concertId);
        redisTemplate.opsForZSet().remove(redisKey, tokenId);
    }

    @Override
    public List<Long> getWaitingConcerts() {
        Set<String> waitingConcertSet = redisTemplate.opsForSet().members(REDIS_CONCERT_WAITING_CONCERTS_KEY);
        if (CollectionUtils.isEmpty(waitingConcertSet)) {
            return Collections.emptyList();
        }
        return waitingConcertSet.stream()
                .map(Long::parseLong)
                .toList();
    }

    @Override
    public void addWaitingConcert(long concertId) {
        redisTemplate.opsForSet().add(REDIS_CONCERT_WAITING_CONCERTS_KEY, String.valueOf(concertId));
    }

    @Override
    public void removeWaitingConcert(long concertId) {
        redisTemplate.opsForSet().remove(REDIS_CONCERT_WAITING_CONCERTS_KEY, String.valueOf(concertId));
    }

}
