package com.sion.concertbooking.domain.watingqueue;

public record WaitingQueueDetailInfo(
        String tokenId,
        long concertId,
        int remainingWaitingOrder,
        int remainingWaitingSec
) {
}
