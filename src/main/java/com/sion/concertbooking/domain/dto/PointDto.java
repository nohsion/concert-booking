package com.sion.concertbooking.domain.dto;

import com.sion.concertbooking.domain.entity.Point;

import java.time.LocalDateTime;

public record PointDto(
        long id,
        long userId,
        int point,
        LocalDateTime updatedAt
) {
    public static PointDto fromEntity(Point point) {
        return new PointDto(point.getId(), point.getUserId(), point.getAmount(), point.getUpdatedAt());
    }
}
