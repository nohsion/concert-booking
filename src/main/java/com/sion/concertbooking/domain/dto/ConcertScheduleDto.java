package com.sion.concertbooking.domain.dto;

import com.sion.concertbooking.domain.entity.ConcertSchedule;

import java.time.LocalDateTime;

public record ConcertScheduleDto(
        long concertScheduleId,
        long concertId,
        LocalDateTime playDateTime
) {
    public static ConcertScheduleDto fromEntity(ConcertSchedule concertSchedule) {
        return new ConcertScheduleDto(
                concertSchedule.getId(),
                concertSchedule.getConcertId(),
                concertSchedule.getPlayDateTime()
        );
    }
}
