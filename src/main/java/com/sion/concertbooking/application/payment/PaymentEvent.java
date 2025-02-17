package com.sion.concertbooking.application.payment;

import com.sion.concertbooking.domain.reservation.ReservationInfo;

import java.util.List;

public record PaymentEvent(
        String tokenId,
        long concertId,
        long userId,
        int totalPrice,
        List<ReservationInfo> reservations
) {
}
