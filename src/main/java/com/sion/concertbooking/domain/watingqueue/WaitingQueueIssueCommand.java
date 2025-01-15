package com.sion.concertbooking.domain.watingqueue;

import java.time.LocalDateTime;

public record WaitingQueueIssueCommand(
        String tokenId,
        long userId,
        long concertId,
        LocalDateTime now
) {
}
