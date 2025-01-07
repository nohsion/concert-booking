package com.sion.concertbooking.application.criteria;

import java.util.List;

public record PaymentCriteria(
        long userId,
        String tokenId,
        List<Long> reservationIds
) {
}
