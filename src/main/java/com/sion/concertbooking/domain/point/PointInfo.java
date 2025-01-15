package com.sion.concertbooking.domain.point;

import java.time.LocalDateTime;

public record PointInfo(
        long id,
        long userId,
        int amount,
        LocalDateTime updatedAt
) {
    public static PointInfo fromEntity(Point point) {
        return new PointInfo(point.getId(), point.getUserId(), point.getAmount(), point.getUpdatedAt());
    }
}
