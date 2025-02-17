package com.sion.concertbooking.domain.dataplatform;

import com.sion.concertbooking.domain.reservation.ReservationInfo;

import java.util.List;

public interface DataPlatformClient {

    void sendReservationPayment(long userId, int totalPrice, List<ReservationInfo> reservations);
}
