package com.sion.concertbooking.domain.repository;

import com.sion.concertbooking.domain.entity.Point;

import java.util.Optional;

public interface PointRepository {
    Optional<Point> findById(long pointId);
    Point findByUserId(long userId);
    Point findByUserIdWithLock(long userId);
    Point save(Point point);
}
