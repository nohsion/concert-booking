package com.sion.concertbooking.domain.info;

public record WaitingQueueDetailInfo(
        String tokenId,
        int remainingWaitingOrder,
        int remainingWaitingSec
) {
}
