package com.sion.concertbooking.application.criteria;

import com.sion.concertbooking.application.PaymentType;

public record PointChargeCriteria(
        long userId,
        int amount,
        PaymentType paymentType
) {
}
