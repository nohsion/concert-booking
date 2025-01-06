package com.sion.concertbooking.presentation.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sion.concertbooking.domain.dto.PointDto;

import java.time.LocalDateTime;

public record PointResponse(
        @JsonProperty(value = "userId") Long userId,
        @JsonProperty(value = "point") int point,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonProperty(value = "updatedAt") LocalDateTime updatedAt
) {
        public static PointResponse fromDto(PointDto dto) {
                return new PointResponse(dto.userId(), dto.point(), dto.updatedAt());
        }
}
