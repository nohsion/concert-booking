package com.sion.concertbooking.infrastructure.payment;

import com.sion.concertbooking.domain.payment.PaymentCharger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FreePaymentCharger implements PaymentCharger {

    @Override
    public void payment(int amount) {
        log.info("Free pay 충전금액: {}", amount);
    }
}
