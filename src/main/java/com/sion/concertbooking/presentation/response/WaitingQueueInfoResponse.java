package com.sion.concertbooking.presentation.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sion.concertbooking.domain.info.WaitingQueueDetailInfo;

public record WaitingQueueInfoResponse(
        @JsonProperty(value = "tokenId") String tokenId,
        @JsonProperty(value = "remainingWaitingOrder") int remainingWaitingOrder,
        @JsonProperty(value = "remainingWaitingSec") int remainingWaitingSec
) {
    public static WaitingQueueInfoResponse fromInfo(WaitingQueueDetailInfo dto) {
        return new WaitingQueueInfoResponse(
                dto.tokenId(), dto.remainingWaitingOrder(), dto.remainingWaitingSec()
        );
    }
}
