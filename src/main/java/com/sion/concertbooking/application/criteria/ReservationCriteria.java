package com.sion.concertbooking.application.criteria;

import java.util.List;

public record ReservationCriteria(
        long userId,
        long concertId,
        long concertScheduleId,
        List<Long> seatIds
) {
}
