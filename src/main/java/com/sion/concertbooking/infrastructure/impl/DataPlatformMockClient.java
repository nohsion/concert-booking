package com.sion.concertbooking.infrastructure.impl;

import com.sion.concertbooking.domain.dataplatform.DataPlatformClient;
import com.sion.concertbooking.domain.reservation.ReservationInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class DataPlatformMockClient implements DataPlatformClient {

    @Override
    public void sendReservationPayment(final long userId, final int totalPrice, final List<ReservationInfo> reservations) {
        log.info("데이터 플랫폼에 결제 정보를 전송합니다. userId={}, totalPrice={}, reservations={}", userId, totalPrice, reservations);
    }
}
