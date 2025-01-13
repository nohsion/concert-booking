package com.sion.concertbooking.infrastructure.repository;

import com.sion.concertbooking.domain.entity.PointHistory;
import com.sion.concertbooking.domain.enums.TransactionType;
import com.sion.concertbooking.domain.repository.PointHistoryRepository;
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
    public Page<PointHistory> findByPointIdAndType(final long pointId, final TransactionType type, final Pageable pageable) {
        return pointHistoryJpaRepository.findByPointIdAndType(pointId, type, pageable);
    }

    @Override
    public Page<PointHistory> findByPointId(final long pointId, final Pageable pageable) {
        return pointHistoryJpaRepository.findByPointId(pointId, pageable);
    }
}
