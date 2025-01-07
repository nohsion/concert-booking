package com.sion.concertbooking.application;

import com.sion.concertbooking.domain.model.info.PointInfo;
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

    public PointInfo chargePoint(long userId, int amount, PaymentType paymentType) {
        // 1. 외부 결제수단 결제 처리
        PaymentCharger paymentCharger = pointChargerResolver.resolve(paymentType);
        paymentCharger.payment(amount);

        // 2. 포인트 충전 처리
        long chargedPointId = pointService.chargePoint(userId, amount);

        // 3. 포인트 충전 이력 저장
        long chargedPointHistoryId = pointHistoryService.chargePointHistory(chargedPointId, amount);

        return pointService.getPointByUserId(userId); // todo: facade용 DTO로 변환 (포인트+이력)
    }

}
