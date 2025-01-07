package com.sion.concertbooking.application.result;

import com.sion.concertbooking.domain.info.ReservationInfo;

import java.util.List;

public record PaymentResult(
        long userId,
        int pointAmount,
        int pointBalance,
        List<ReservationInfo> reservations
) {
}
