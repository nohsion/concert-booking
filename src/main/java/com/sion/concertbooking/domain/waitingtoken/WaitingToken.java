package com.sion.concertbooking.domain.waitingtoken;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaitingToken {

    private static final int EXPIRED_MINUTES = 30;

    private String tokenId;

    private long userId;

    private long concertId;

    private LocalDateTime createdAt;

    public static WaitingToken of(String tokenId, long userId, long concertId, LocalDateTime now) {
        WaitingToken waitingQueue = new WaitingToken();
        waitingQueue.tokenId = tokenId;
        waitingQueue.userId = userId;
        waitingQueue.concertId = concertId;
        waitingQueue.createdAt = now;
        return waitingQueue;
    }

    public int getExpiredMinutes() {
        return EXPIRED_MINUTES;
    }
}
