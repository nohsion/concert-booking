package com.sion.concertbooking.intefaces.presentation.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sion.concertbooking.application.waitingqueue.WaitingQueueDetailResult;

public record WaitingQueueInfoResponse(
        @JsonProperty(value = "tokenId") String tokenId,
        @JsonProperty(value = "remainingWaitingOrder") int remainingWaitingOrder,
        @JsonProperty(value = "remainingWaitingSec") int remainingWaitingSec
) {
    public static WaitingQueueInfoResponse fromResult(WaitingQueueDetailResult result) {
        return new WaitingQueueInfoResponse(
                result.tokenId(), result.remainingWaitingOrder(), result.remainingWaitingSec()
        );
    }
}
