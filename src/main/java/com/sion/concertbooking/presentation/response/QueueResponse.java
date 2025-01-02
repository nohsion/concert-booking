package com.sion.concertbooking.presentation.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record QueueResponse(
        @JsonProperty(value = "tokenId") String tokenId,
        @JsonProperty(value = "remainingWaitingOrder") int remainingWaitingOrder,
        @JsonProperty(value = "remainingWaitingSec") int remainingWaitingSec
) {
}
