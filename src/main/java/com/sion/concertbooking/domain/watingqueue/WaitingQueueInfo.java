package com.sion.concertbooking.domain.watingqueue;

import java.time.LocalDateTime;

public record WaitingQueueInfo(
        String tokenId,
        long userId,
        long concertId,
        LocalDateTime createdAt
) {
    public static WaitingQueueInfo fromEntity(WaitingQueue entity) {
        return new WaitingQueueInfo(
                entity.getTokenId(),
                entity.getUserId(),
                entity.getConcertId(),
                entity.getCreatedAt()
        );
    }
}
