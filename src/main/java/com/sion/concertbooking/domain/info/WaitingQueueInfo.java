package com.sion.concertbooking.domain.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sion.concertbooking.domain.entity.WaitingQueue;
import com.sion.concertbooking.domain.enums.WaitingQueueStatus;

import java.time.LocalDateTime;

public record WaitingQueueInfo(
        @JsonProperty(value = "waitingQueueId") long waitingQueueId,
        @JsonProperty(value = "tokenId") String tokenId,
        @JsonProperty(value = "userId") long userId,
        @JsonProperty(value = "concertId") long concertId,
        @JsonProperty(value = "status") WaitingQueueStatus status,
        @JsonProperty(value = "createdAt") LocalDateTime createdAt,
        @JsonProperty(value = "expiredAt") LocalDateTime expiredAt
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
