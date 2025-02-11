package com.sion.concertbooking.domain.watingqueue;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaitingQueue {

    private String tokenId;

    private long userId;

    private long concertId;

    private LocalDateTime createdAt;

    public static WaitingQueue of(String tokenId, long userId, long concertId, LocalDateTime now) {
        WaitingQueue waitingQueue = new WaitingQueue();
        waitingQueue.tokenId = tokenId;
        waitingQueue.userId = userId;
        waitingQueue.concertId = concertId;
        waitingQueue.createdAt = now;
        return waitingQueue;
    }
}
