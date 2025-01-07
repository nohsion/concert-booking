package com.sion.concertbooking.application;

import com.sion.concertbooking.domain.model.info.PointInfo;
import com.sion.concertbooking.domain.service.PointHistoryService;
import com.sion.concertbooking.domain.service.PointService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PointDeductor {

    private final PointService pointService;
    private final PointHistoryService pointHistoryService;

    public PointDeductor(PointService pointService, PointHistoryService pointHistoryService) {
        this.pointService = pointService;
        this.pointHistoryService = pointHistoryService;
    }

    @Transactional
    public PointInfo usePoint(long userId, int amount) {
        // 1. 포인트 사용
        long usedPointId = pointService.usePoint(userId, amount);
        // 2. 포인트 사용 이력 저장
        long usedPointHistoryId = pointHistoryService.usePointHistory(usedPointId, amount);
        return pointService.getPointByUserId(userId);
    }
}
