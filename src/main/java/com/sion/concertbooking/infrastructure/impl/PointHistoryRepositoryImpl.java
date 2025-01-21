package com.sion.concertbooking.infrastructure.impl;

import com.sion.concertbooking.domain.pointhistory.PointHistory;
import com.sion.concertbooking.domain.pointhistory.PointHistoryRepository;
import com.sion.concertbooking.infrastructure.jpa.PointHistoryJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PointHistoryRepositoryImpl implements PointHistoryRepository {

    private final PointHistoryJpaRepository pointHistoryJpaRepository;

    public PointHistoryRepositoryImpl(final PointHistoryJpaRepository pointHistoryJpaRepository) {
        this.pointHistoryJpaRepository = pointHistoryJpaRepository;
    }

    @Override
    public Optional<PointHistory> findById(final long pointHistoryId) {
        return pointHistoryJpaRepository.findById(pointHistoryId);
    }

    @Override
    public PointHistory save(final PointHistory pointHistory) {
        return pointHistoryJpaRepository.save(pointHistory);
    }

    @Override
    public Page<PointHistory> findByPointIdAndType(final long pointId, final PointHistory.TransactionType type, final Pageable pageable) {
        return pointHistoryJpaRepository.findByPointIdAndType(pointId, type, pageable);
    }

    @Override
    public Page<PointHistory> findByPointId(final long pointId, final Pageable pageable) {
        return pointHistoryJpaRepository.findByPointId(pointId, pageable);
    }
}
