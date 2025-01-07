package com.sion.concertbooking.domain.model.info;

import com.sion.concertbooking.domain.model.entity.Point;

import java.time.LocalDateTime;

public record PointInfo(
        long id,
        long userId,
        int point,
        LocalDateTime updatedAt
) {
    public static PointInfo fromEntity(Point point) {
        return new PointInfo(point.getId(), point.getUserId(), point.getAmount(), point.getUpdatedAt());
    }
}
