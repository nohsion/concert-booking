package com.sion.concertbooking.domain.repository;

import com.sion.concertbooking.domain.entity.PointHistory;
import com.sion.concertbooking.domain.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PointHistoryRepository {
    Optional<PointHistory> findById(long pointHistoryId);
    PointHistory save(PointHistory pointHistory);
    Page<PointHistory> findByPointIdAndType(long pointId, TransactionType type, Pageable pageable);
    Page<PointHistory> findByPointId(long pointId, Pageable pageable);
}
