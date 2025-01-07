package com.sion.concertbooking.domain.service;

import com.sion.concertbooking.domain.model.entity.PointHistory;
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
}
