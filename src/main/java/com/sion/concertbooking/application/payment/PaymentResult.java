package com.sion.concertbooking.application.payment;

import com.sion.concertbooking.domain.reservation.ReservationInfo;

import java.util.List;

public record PaymentResult(
        long userId,
        int pointAmount,
        int pointBalance,
        List<ReservationInfo> reservations
) {
}
