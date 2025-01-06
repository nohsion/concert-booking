package com.sion.concertbooking.presentation.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record PointResponse(
        @JsonProperty(value = "userId") Long userId,
        @JsonProperty(value = "point") int point,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonProperty(value = "updatedAt") LocalDateTime updatedAt
) {
}
