package com.sion.concertbooking.application.payment;

import java.util.List;

public record PaymentCriteria(
        long userId,
        String tokenId,
        List<Long> reservationIds,
        long concertId
) {
}
