package com.sion.concertbooking.application.point;

import com.sion.concertbooking.application.payment.PaymentType;

public record PointChargeCriteria(
        long userId,
        int amount,
        PaymentType paymentType
) {
}
