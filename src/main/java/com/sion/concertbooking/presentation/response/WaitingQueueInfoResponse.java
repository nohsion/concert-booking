package com.sion.concertbooking.presentation.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sion.concertbooking.domain.dto.WaitingQueueDetailInfo;

public record WaitingQueueInfoResponse(
        @JsonProperty(value = "tokenId") String tokenId,
        @JsonProperty(value = "remainingWaitingOrder") int remainingWaitingOrder,
        @JsonProperty(value = "remainingWaitingSec") int remainingWaitingSec
) {
    public static WaitingQueueInfoResponse fromDto(WaitingQueueDetailInfo dto) {
        return new WaitingQueueInfoResponse(dto.tokenId(), dto.remainingWaitingOrder(), dto.remainingWaitingSec());
    }
}
