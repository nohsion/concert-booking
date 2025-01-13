package com.sion.concertbooking.application.point;

import com.sion.concertbooking.application.payment.PaymentType;
import com.sion.concertbooking.domain.pointhistory.PointHistoryInfo;
import com.sion.concertbooking.domain.point.PointInfo;
import com.sion.concertbooking.domain.payment.PaymentCharger;
import com.sion.concertbooking.domain.pointhistory.PointHistoryService;
import com.sion.concertbooking.domain.point.PointService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public PointResult.Charge chargePoint(long userId, int amount, PaymentType paymentType) {
        // 1. 외부 결제수단 결제 처리
        PaymentCharger paymentCharger = pointChargerResolver.resolve(paymentType);
        paymentCharger.payment(amount);

        // 2. 포인트 충전 처리
        PointInfo chargedPointInfo = pointService.chargePoint(userId, amount);

        // 3. 포인트 충전 이력 저장
        PointHistoryInfo chargedPointHistory = pointHistoryService.chargePointHistory(chargedPointInfo.id(), amount);

        return PointResult.Charge.of(chargedPointInfo, chargedPointHistory, paymentType);
    }

}
