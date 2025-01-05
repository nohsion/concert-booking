package com.sion.concertbooking.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record UserPointDto(
        @JsonProperty(value = "userId") long userId,
        @JsonProperty(value = "point") int point,
        @JsonProperty(value = "updatedAt") LocalDateTime updatedAt
) {
}
