package com.sion.concertbooking.application.waitingqueue;

import com.sion.concertbooking.domain.watingqueue.WaitingQueueDetailInfo;

public record WaitingQueueDetailResult(
        String tokenId,
        long concertId,
        int remainingWaitingOrder,
        int remainingWaitingSec
) {
    public static WaitingQueueDetailResult fromInfo(WaitingQueueDetailInfo info) {
        return new WaitingQueueDetailResult(info.tokenId(), info.concertId(), info.remainingWaitingOrder(), info.remainingWaitingSec());
    }
}
