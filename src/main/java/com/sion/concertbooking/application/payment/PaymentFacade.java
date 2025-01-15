package com.sion.concertbooking.application.payment;

import com.sion.concertbooking.domain.point.PointInfo;
import com.sion.concertbooking.domain.point.PointService;
import com.sion.concertbooking.domain.reservation.ReservationInfo;
import com.sion.concertbooking.domain.reservation.ReservationService;
import com.sion.concertbooking.domain.watingqueue.WaitingQueueService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PaymentFacade {

    private final PointService pointService;
    private final ReservationService reservationService;
    private final WaitingQueueService waitingQueueService;

    public PaymentFacade(
            PointService pointService,
            ReservationService reservationService,
            WaitingQueueService waitingQueueService
    ) {
        this.pointService = pointService;
        this.reservationService = reservationService;
        this.waitingQueueService = waitingQueueService;
    }

    @Transactional
    public PaymentResult processPayment(PaymentCriteria criteria) {
        // 임시 배정된 좌석이 만료되지는 않았는지 확인한다.
        LocalDateTime now = LocalDateTime.now();
        List<Long> reservationIds = criteria.reservationIds();

        boolean hasExpiredReservations = reservationService.checkExpiredReservations(reservationIds, now);
        if (hasExpiredReservations) {
            throw new IllegalArgumentException("좌석 선점 시간이 만료되었습니다. 다시 시도해주세요.");
        }

        List<ReservationInfo> reservations = reservationService.getReservationsById(reservationIds);
        int totalPrice = reservations.stream()
                .mapToInt(ReservationInfo::seatPrice)
                .sum();

        // 예약한 가격만큼 포인트를 차감한다.
        long userId = criteria.userId();
        PointInfo currentPoint = pointService.usePoint(userId, totalPrice);

        // 예약 상태를 결제 완료로 변경한다.
        reservationService.completeReservations(reservationIds);

        // 사용자의 대기열 토큰을 만료 시킨다.
        waitingQueueService.expireToken(criteria.tokenId());

        return new PaymentResult(userId, totalPrice, currentPoint.amount(), reservations);
    }

}
