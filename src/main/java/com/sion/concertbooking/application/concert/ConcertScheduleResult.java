package com.sion.concertbooking.application.concert;

import com.sion.concertbooking.domain.concertschedule.ConcertScheduleInfo;

import java.time.LocalDateTime;

public record ConcertScheduleResult(
        long concertScheduleId,
        long concertId,
        LocalDateTime playDateTime,
        int remainingSeatCount
) {
    public static ConcertScheduleResult fromInfo(ConcertScheduleInfo concertScheduleInfo,
                                                 int remainingSeatCount) {
        return new ConcertScheduleResult(
                concertScheduleInfo.concertScheduleId(),
                concertScheduleInfo.concertId(),
                concertScheduleInfo.playDateTime(),
                remainingSeatCount
        );
    }
}
