package com.sion.concertbooking.application.waitingqueue;

import com.sion.concertbooking.domain.watingqueue.WaitingQueueInfo;
import com.sion.concertbooking.domain.watingqueue.WaitingQueueStatus;

import java.time.LocalDateTime;

public record WaitingQueueResult(
        long waitingQueueId,
        String tokenId,
        long userId,
        long concertId,
        WaitingQueueStatus status,
        LocalDateTime createdAt,
        LocalDateTime expiredAt
) {
    public static WaitingQueueResult fromInfo(WaitingQueueInfo info) {
        return new WaitingQueueResult(
                info.waitingQueueId(),
                info.tokenId(),
                info.userId(),
                info.concertId(),
                info.status(),
                info.createdAt(),
                info.expiredAt()
        );
    }
}
