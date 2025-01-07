package com.sion.concertbooking.domain.service;

import com.sion.concertbooking.domain.entity.PointHistory;
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
    public PointHistory chargePointHistory(long pointId, int amount) {
        PointHistory pointHistoryToCharge = PointHistory.ofCharge(pointId, amount);
        return pointHistoryRepository.save(pointHistoryToCharge);
    }

    @Transactional
    public PointHistory usePointHistory(long pointId, int amount) {
        PointHistory pointHistoryToUse = PointHistory.ofUse(pointId, amount);
        return pointHistoryRepository.save(pointHistoryToUse);
    }
}
