package com.sion.concertbooking.domain.pointhistory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class PointHistoryService {

    private final PointHistoryRepository pointHistoryRepository;

    public PointHistoryService(PointHistoryRepository pointHistoryRepository) {
        this.pointHistoryRepository = pointHistoryRepository;
    }

    @Transactional
    public PointHistoryInfo chargePointHistory(long pointId, int amount) {
        PointHistory pointHistoryToCharge = PointHistory.ofCharge(pointId, amount);
        PointHistory chargedPointHistory = pointHistoryRepository.save(pointHistoryToCharge);
        return PointHistoryInfo.fromEntity(chargedPointHistory);
    }

    @Transactional
    public PointHistoryInfo usePointHistory(long pointId, int amount) {
        PointHistory pointHistoryToUse = PointHistory.ofUse(pointId, amount);
        PointHistory usedPointHistory = pointHistoryRepository.save(pointHistoryToUse);
        return PointHistoryInfo.fromEntity(usedPointHistory);
    }

    public PointHistoryInfo getPointHistoryById(long pointHistoryId) {
        PointHistory pointHistory = pointHistoryRepository.findById(pointHistoryId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 포인트 이력입니다. pointHistoryId=" + pointHistoryId));
        return PointHistoryInfo.fromEntity(pointHistory);
    }

    public Page<PointHistoryInfo> getChargedPointHistories(long pointId, Pageable pageable) {
        Page<PointHistory> pagedPointHistories = pointHistoryRepository.findByPointIdAndType(
                pointId, PointHistory.TransactionType.CHARGE, pageable);
        return pagedPointHistories.map(PointHistoryInfo::fromEntity);
    }

    public Page<PointHistoryInfo> getUsedPointHistories(long pointId, Pageable pageable) {
        Page<PointHistory> pagedPointHistories = pointHistoryRepository.findByPointIdAndType(
                pointId, PointHistory.TransactionType.USE, pageable);
        return pagedPointHistories.map(PointHistoryInfo::fromEntity);
    }

    public Page<PointHistoryInfo> getPointHistories(long pointId, Pageable pageable) {
        Page<PointHistory> pagedPointHistories = pointHistoryRepository.findByPointId(pointId, pageable);
        return pagedPointHistories.map(PointHistoryInfo::fromEntity);
    }
}
