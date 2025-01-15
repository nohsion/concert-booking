package com.sion.concertbooking.domain.watingqueue;

import java.time.LocalDateTime;

public record WaitingQueueInfo(
        long waitingQueueId,
        String tokenId,
        long userId,
        long concertId,
        WaitingQueueStatus status,
        LocalDateTime createdAt,
        LocalDateTime expiredAt
) {
    public static WaitingQueueInfo fromEntity(WaitingQueue entity) {
        return new WaitingQueueInfo(
                entity.getId(),
                entity.getTokenId(),
                entity.getUserId(),
                entity.getConcertId(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getExpiredAt()
        );
    }
}
