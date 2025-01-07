package com.sion.concertbooking.domain.model.info;

import com.sion.concertbooking.domain.model.entity.ConcertSchedule;

import java.time.LocalDateTime;

public record ConcertScheduleInfo(
        long concertScheduleId,
        long concertId,
        LocalDateTime playDateTime
) {
    public static ConcertScheduleInfo fromEntity(ConcertSchedule concertSchedule) {
        return new ConcertScheduleInfo(
                concertSchedule.getId(),
                concertSchedule.getConcertId(),
                concertSchedule.getPlayDateTime()
        );
    }
}
