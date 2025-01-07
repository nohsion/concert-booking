package com.sion.concertbooking.domain.repository;

import com.sion.concertbooking.domain.model.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {
    Point findByUserId(long userId);
}
