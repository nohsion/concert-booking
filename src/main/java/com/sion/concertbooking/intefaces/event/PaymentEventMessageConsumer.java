package com.sion.concertbooking.intefaces.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PaymentEventMessageConsumer {

    public void consume(List<String> messages) {
        // 사실, 이건 데이터 플랫폼에서 처리해야 하는 일이다.
        log.info("PaymentEventMessageConsumer consume messages: {}", messages);
    }
}
