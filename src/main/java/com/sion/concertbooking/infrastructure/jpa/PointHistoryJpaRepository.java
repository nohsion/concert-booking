package com.sion.concertbooking.infrastructure.jpa;

import com.sion.concertbooking.domain.pointhistory.PointHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryJpaRepository extends JpaRepository<PointHistory, Long> {
    Page<PointHistory> findByPointIdAndType(long pointId, PointHistory.TransactionType type, Pageable pageable);
    Page<PointHistory> findByPointId(long pointId, Pageable pageable);
}
