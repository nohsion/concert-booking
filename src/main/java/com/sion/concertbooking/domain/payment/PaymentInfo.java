package com.sion.concertbooking.domain.payment;

public record PaymentInfo(
    long id,
    long userId,
    long amount
) {
    public static PaymentInfo fromEntity(Payment payment) {
        return new PaymentInfo(
                payment.getId(),
                payment.getUserId(),
                payment.getAmount()
        );
    }
}
