package com.sion.concertbooking.domain.payment;

public interface PaymentEventSender {

    void send(PaymentEventMessage message);
}
