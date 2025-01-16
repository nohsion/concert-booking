package com.sion.concertbooking.application.waitingqueue;

import java.time.LocalDateTime;

public record WaitingQueueIssueCriteria(
        long userId,
        long concertId,
        LocalDateTime now
) {
}
