package com.sion.concertbooking.domain.point;

import java.util.Optional;

public interface PointRepository {
    Optional<Point> findById(long pointId);
    Point findByUserId(long userId);
    Point findByUserIdWithLock(long userId);
    Point save(Point point);
}
