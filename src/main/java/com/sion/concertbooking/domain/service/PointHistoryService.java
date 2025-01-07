package com.sion.concertbooking.domain.service;

import com.sion.concertbooking.domain.entity.PointHistory;
import com.sion.concertbooking.domain.info.PointHistoryInfo;
import com.sion.concertbooking.domain.repository.PointHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PointHistoryService {

    private final PointHistoryRepository pointHistoryRepository;

    public PointHistoryService(PointHistoryRepository pointHistoryRepository) {
        this.pointHistoryRepository = pointHistoryRepository;
    }

    @Transactional
    public long chargePointHistory(long pointId, int amount) {
        PointHistory pointHistoryToCharge = PointHistory.ofCharge(pointId, amount);
        PointHistory chargedPointHistory = pointHistoryRepository.save(pointHistoryToCharge);
        return chargedPointHistory.getId();
    }

    @Transactional
    public long usePointHistory(long pointId, int amount) {
        PointHistory pointHistoryToUse = PointHistory.ofUse(pointId, amount);
        PointHistory usedPointHistory = pointHistoryRepository.save(pointHistoryToUse);
        return usedPointHistory.getId();
    }

    public PointHistoryInfo getPointHistoryById(long pointHistoryId) {
        PointHistory pointHistory = pointHistoryRepository.findById(pointHistoryId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 포인트 이력입니다."));
        return PointHistoryInfo.fromEntity(pointHistory);
    }
}
