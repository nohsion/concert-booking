package com.sion.concertbooking.domain.activequeue;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActiveQueue {

    private static final int EXPIRED_MINUTES = 5;

    private String tokenId;

    private long userId;

    private long concertId;

    private LocalDateTime expiredAt;

    private ActiveQueue(String tokenId, long concertId, LocalDateTime expiredAt) {
        this.tokenId = tokenId;
        this.concertId = concertId;
        this.expiredAt = expiredAt;
    }

    private ActiveQueue(String tokenId, long userId, long concertId, LocalDateTime expiredAt) {
        this.tokenId = tokenId;
        this.userId = userId;
        this.concertId = concertId;
        this.expiredAt = expiredAt;
    }

    public static ActiveQueue of(String tokenId, long userId, long concertId, LocalDateTime now) {
        return new ActiveQueue(tokenId, userId, concertId, now.plusMinutes(EXPIRED_MINUTES));
    }

    public static ActiveQueue of(String tokenId, long concertId, LocalDateTime now) {
        return new ActiveQueue(tokenId, concertId, now.plusMinutes(EXPIRED_MINUTES));
    }

    public boolean isExpiredTime(LocalDateTime now) {
        return this.expiredAt.isBefore(now);
    }
}
