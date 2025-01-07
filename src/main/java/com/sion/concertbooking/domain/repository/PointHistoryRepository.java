package com.sion.concertbooking.domain.repository;

import com.sion.concertbooking.domain.model.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
    PointHistory findByPointId(long pointId);
}
