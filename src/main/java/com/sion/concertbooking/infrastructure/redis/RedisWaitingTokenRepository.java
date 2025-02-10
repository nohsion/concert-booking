package com.sion.concertbooking.infrastructure.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.sion.concertbooking.domain.waitingtoken.WaitingToken;
import com.sion.concertbooking.domain.waitingtoken.WaitingTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
public class RedisWaitingTokenRepository implements WaitingTokenRepository {

    private static final String REDIS_WAITING_TOKEN_KEY_FORMAT = "WAITING_TOKEN:%s";
    private static final TypeReference<WaitingToken> WAITING_TOKEN_TYPE_REFERENCE = new TypeReference<>() {};

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final ObjectMapper OBJECT_MAPPER = Jackson2ObjectMapperBuilder.json()
            .serializers(new LocalDateTimeSerializer(DATE_TIME_FORMATTER))
            .deserializers(new LocalDateTimeDeserializer(DATE_TIME_FORMATTER))
            .build();

    private final RedisTemplate<String, String> redisTemplate;

    public RedisWaitingTokenRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public WaitingToken save(WaitingToken waitingToken) {
        String redisKey = String.format(REDIS_WAITING_TOKEN_KEY_FORMAT, waitingToken.getTokenId());

        String waitingTokenJson;
        try {
            waitingTokenJson = OBJECT_MAPPER.writeValueAsString(waitingToken);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize object to JSON", e);
            throw new RuntimeException(e);
        }

        redisTemplate.opsForValue().set(redisKey, waitingTokenJson, waitingToken.getExpiredMinutes(), TimeUnit.MINUTES);
        return waitingToken;
    }

    @Override
    public Optional<WaitingToken> findByTokenId(String tokenId) {
        String redisKey = String.format(REDIS_WAITING_TOKEN_KEY_FORMAT, tokenId);
        String waitingTokenJson = redisTemplate.opsForValue().get(redisKey);
        if (!StringUtils.hasText(waitingTokenJson)) {
            return Optional.empty();
        }
        try {
            WaitingToken waitingToken = OBJECT_MAPPER.readValue(waitingTokenJson, WAITING_TOKEN_TYPE_REFERENCE);
            return Optional.of(waitingToken);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize JSON to object", e);
            return Optional.empty();
        }
    }

}
