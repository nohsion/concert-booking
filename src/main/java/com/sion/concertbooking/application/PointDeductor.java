package com.sion.concertbooking.application;

import com.sion.concertbooking.application.result.PointResult;
import com.sion.concertbooking.domain.info.PointHistoryInfo;
import com.sion.concertbooking.domain.info.PointInfo;
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
    public PointResult.Use usePoint(long userId, int amount) {
        // 1. 포인트 사용
        PointInfo pointInfo = pointService.usePoint(userId, amount);
        // 2. 포인트 사용 이력 저장
        PointHistoryInfo pointHistoryInfo = pointHistoryService.usePointHistory(pointInfo.id(), amount);

        return PointResult.Use.of(pointInfo, pointHistoryInfo);
    }
}
