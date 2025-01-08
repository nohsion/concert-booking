package com.sion.concertbooking.infrastructure.payment;

import com.sion.concertbooking.domain.service.PaymentCharger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KakaoPaymentCharger implements PaymentCharger {

    @Override
    public void payment(final int amount) {
        log.info("KaKao pay 충전금액: {}", amount);
    }
}
