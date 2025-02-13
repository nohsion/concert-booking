package com.sion.concertbooking.infrastructure.impl;

import com.sion.concertbooking.domain.dataplatform.DataPlatformClient;
import com.sion.concertbooking.domain.reservation.ReservationInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class DataPlatformMockClient implements DataPlatformClient {

    private static final int MAX_DELAY_MILLIS = 1000;
    private final Random random = new Random();

    @Override
    public void sendReservationPayment(final long userId, final int totalPrice, final List<ReservationInfo> reservations) {
        log.info("데이터 플랫폼에 결제 정보를 전송합니다. userId={}, totalPrice={}, reservations={}", userId, totalPrice, reservations);
        throttle(MAX_DELAY_MILLIS);
        throw new IllegalStateException("데이터 플랫폼 API 연동에 실패했습니다.");
    }

    private void throttle(int millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(random.nextInt(millis));
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
