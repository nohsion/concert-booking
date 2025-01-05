package com.sion.concertbooking.domain.dto;

public record WaitingQueueInfo(
        String tokenId,
        int remainingWaitingOrder,
        int remainingWaitingSec
) {
}
