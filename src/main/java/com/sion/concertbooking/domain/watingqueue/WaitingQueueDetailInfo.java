package com.sion.concertbooking.domain.watingqueue;

public record WaitingQueueDetailInfo(
        String tokenId,
        int remainingWaitingOrder,
        int remainingWaitingSec
) {
}
