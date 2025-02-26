package com.sion.concertbooking.infrastructure.impl;

import com.sion.concertbooking.domain.payment.Payment;
import com.sion.concertbooking.domain.payment.PaymentRepository;
import com.sion.concertbooking.infrastructure.jpa.PaymentJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    public PaymentRepositoryImpl(final PaymentJpaRepository paymentJpaRepository) {
        this.paymentJpaRepository = paymentJpaRepository;
    }

    @Override
    public Payment save(final Payment payment) {
        return paymentJpaRepository.save(payment);
    }
}
