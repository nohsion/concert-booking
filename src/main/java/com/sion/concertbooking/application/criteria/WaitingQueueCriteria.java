package com.sion.concertbooking.application.criteria;

import java.time.LocalDateTime;

public record WaitingQueueCriteria(
        long userId,
        long concertId,
        LocalDateTime registeredAt
) {
}
