package com.sion.concertbooking.domain.repository;

import com.sion.concertbooking.domain.entity.PointHistory;
import com.sion.concertbooking.domain.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
    Page<PointHistory> findByPointIdAndType(long pointId, TransactionType type, Pageable pageable);
    Page<PointHistory> findByPointId(long pointId, Pageable pageable);
}
