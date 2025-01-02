package com.sion.concertbooking.presentation.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ApiErrorResponse(
        @JsonProperty(value = "message") String message
) {
}
