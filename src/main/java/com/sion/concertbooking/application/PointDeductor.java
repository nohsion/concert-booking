package com.sion.concertbooking.application;

import com.sion.concertbooking.domain.dto.PointDto;
import com.sion.concertbooking.domain.entity.Point;
import com.sion.concertbooking.domain.entity.PointHistory;
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
    public PointDto usePoint(long userId, int amount) {
        // 1. 포인트 사용
        Point point = pointService.usePoint(userId, amount);
        // 2. 포인트 사용 이력 저장
        PointHistory pointHistory = pointHistoryService.usePointHistory(point.getId(), amount);
        return PointDto.fromEntity(point);
    }
}
