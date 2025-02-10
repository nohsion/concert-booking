package com.sion.concertbooking.application.waitingqueue;

import com.sion.concertbooking.domain.waitingtoken.WaitingTokenInfo;

import java.time.LocalDateTime;

public record WaitingQueueResult(
        String tokenId,
        long userId,
        long concertId,
        LocalDateTime createdAt
) {
    public static WaitingQueueResult fromInfo(WaitingTokenInfo info) {
        return new WaitingQueueResult(
                info.tokenId(),
                info.userId(),
                info.concertId(),
                info.createdAt()
        );
    }
}
