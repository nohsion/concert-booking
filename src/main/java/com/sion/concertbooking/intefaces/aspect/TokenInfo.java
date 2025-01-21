package com.sion.concertbooking.intefaces.aspect;

import com.sion.concertbooking.domain.watingqueue.WaitingQueue;

import java.time.LocalDateTime;

public record TokenInfo(
        String tokenId,
        long userId,
        long concertId,
        WaitingQueue.Status status,
        LocalDateTime expiredAt
) {
}
