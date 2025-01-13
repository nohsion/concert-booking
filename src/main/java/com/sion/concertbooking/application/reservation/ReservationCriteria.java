package com.sion.concertbooking.application.reservation;

import java.util.List;

public record ReservationCriteria(
        long userId,
        long concertId,
        long concertScheduleId,
        List<Long> seatIds
) {
}
