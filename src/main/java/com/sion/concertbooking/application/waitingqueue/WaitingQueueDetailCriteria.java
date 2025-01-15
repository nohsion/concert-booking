package com.sion.concertbooking.application.waitingqueue;

import java.time.LocalDateTime;

public record WaitingQueueDetailCriteria(
        String tokenId,
        LocalDateTime now
) {
}
