package com.sion.concertbooking.application;

import com.sion.concertbooking.domain.dto.PointDto;
import com.sion.concertbooking.domain.entity.Point;
import com.sion.concertbooking.domain.entity.PointHistory;
import com.sion.concertbooking.domain.service.PaymentCharger;
import com.sion.concertbooking.domain.service.PointHistoryService;
import com.sion.concertbooking.domain.service.PointService;
import org.springframework.stereotype.Component;

@Component
public class PointCharger {

    private final PointService pointService;
    private final PointHistoryService pointHistoryService;
    private final PointChargerResolver pointChargerResolver;

    public PointCharger(
            PointService pointService,
            PointHistoryService pointHistoryService,
            PointChargerResolver pointChargerResolver
    ) {
        this.pointService = pointService;
        this.pointHistoryService = pointHistoryService;
        this.pointChargerResolver = pointChargerResolver;
    }

    public PointDto chargePoint(long userId, int amount, PaymentType paymentType) {
        // 1. 외부 결제수단 결제 처리
        PaymentCharger paymentCharger = pointChargerResolver.resolve(paymentType);
        paymentCharger.payment(amount);

        // 2. 포인트 충전 처리
        Point chargedPoint = pointService.chargePoint(userId, amount);

        // 3. 포인트 충전 이력 저장
        PointHistory chargedPointHistory = pointHistoryService.chargePointHistory(chargedPoint.getId(), amount);

        return PointDto.fromEntity(chargedPoint);
    }

}
