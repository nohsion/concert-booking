package com.sion.concertbooking.domain.payment;

public record PaymentCreateCommand(
        long userId,
        long amount
) {

    public Payment toEntity() {
        return Payment.of(userId, amount);
    }
}
