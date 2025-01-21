package com.sion.concertbooking.application.point;

import com.sion.concertbooking.application.payment.PaymentType;
import com.sion.concertbooking.domain.pointhistory.PointHistory;
import com.sion.concertbooking.domain.pointhistory.PointHistoryInfo;
import com.sion.concertbooking.domain.point.PointInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PointResult {

    @Getter
    @RequiredArgsConstructor
    public static class Charge {
        private final long userId;
        private final int amount;
        private final int balance;
        private final PointHistory.TransactionType transactionType = PointHistory.TransactionType.CHARGE;
        private final PaymentType paymentType;
        private final LocalDateTime updatedAt;

        public static Charge of(
                PointInfo pointInfo, PointHistoryInfo pointHistoryInfo, PaymentType paymentType
        ) {
            return new Charge(
                    pointInfo.userId(),
                    pointHistoryInfo.amount(),
                    pointInfo.amount(),
                    paymentType,
                    pointHistoryInfo.updatedAt()
            );
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class Use {
        private final long userId;
        private final int amount;
        private final int balance;
        private final PointHistory.TransactionType transactionType = PointHistory.TransactionType.USE;
        private final LocalDateTime updatedAt;

        public static Use of(
                PointInfo pointInfo, PointHistoryInfo pointHistoryInfo
        ) {
            return new Use(
                    pointInfo.userId(),
                    pointHistoryInfo.amount(),
                    pointInfo.amount(),
                    pointHistoryInfo.updatedAt()
            );
        }
    }
}
