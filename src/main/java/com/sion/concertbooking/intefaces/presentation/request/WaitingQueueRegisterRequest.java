package com.sion.concertbooking.intefaces.presentation.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WaitingQueueRegisterRequest(
        @JsonProperty(value = "userId") long userId,
        @JsonProperty(value = "concertId") long concertId
) {
}
