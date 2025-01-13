package com.sion.concertbooking.infrastructure.repository;

import jakarta.persistence.LockModeType;
import com.sion.concertbooking.domain.point.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PointJpaRepository extends JpaRepository<Point, Long> {
    Point findByUserId(long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Point p WHERE p.userId = :userId")
    Point findByUserIdWithLock(@Param("userId") long userId);
}
