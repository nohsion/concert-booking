package com.sion.concertbooking.infrastructure.repository;

import com.sion.concertbooking.domain.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointJpaRepository extends JpaRepository<Point, Long> {
    Point findByUserId(long userId);
}
