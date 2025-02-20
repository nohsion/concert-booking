package com.sion.concertbooking.domain.payment;

import org.springframework.stereotype.Component;

@Component
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentInfo save(PaymentCreateCommand command) {
        Payment payment = paymentRepository.save(command.toEntity());
        return PaymentInfo.fromEntity(payment);
    }
}
