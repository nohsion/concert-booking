package com.sion.concertbooking.domain.dto;

public record WaitingQueueDetailInfo(
        String tokenId,
        int remainingWaitingOrder,
        int remainingWaitingSec
) {
}
