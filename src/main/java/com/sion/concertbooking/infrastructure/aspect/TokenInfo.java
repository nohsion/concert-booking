package com.sion.concertbooking.infrastructure.aspect;

import com.sion.concertbooking.domain.watingqueue.WaitingQueueStatus;

import java.time.LocalDateTime;

public record TokenInfo(
        String tokenId,
        long userId,
        long concertId,
        WaitingQueueStatus status,
        LocalDateTime expiredAt
) {
}
