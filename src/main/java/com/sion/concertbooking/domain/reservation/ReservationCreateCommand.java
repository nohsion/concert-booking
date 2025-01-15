package com.sion.concertbooking.domain.reservation;

import com.sion.concertbooking.domain.concert.ConcertInfo;
import com.sion.concertbooking.domain.concertschedule.ConcertScheduleInfo;
import com.sion.concertbooking.domain.seat.SeatGrade;
import com.sion.concertbooking.domain.seat.SeatInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReservationCreateCommand {
    private long userId;
    private long concertId;
    private String concertTitle;
    private long concertScheduleId;
    private LocalDateTime playDateTime;
    private LocalDateTime now;
    private List<SeatCreateCommand> seats;

    public static ReservationCreateCommand of(
            long userId,
            ConcertInfo concertInfo,
            ConcertScheduleInfo concertScheduleInfo,
            List<SeatInfo> seatInfos,
            LocalDateTime now
    ) {
        List<ReservationCreateCommand.SeatCreateCommand> seatCreateCommands = seatInfos.stream()
                .map(seatInfo -> new ReservationCreateCommand.SeatCreateCommand(
                        seatInfo.seatId(), seatInfo.seatNum(), seatInfo.seatGrade(), seatInfo.seatPrice())
                )
                .toList();
        return ReservationCreateCommand.builder()
                .userId(userId)
                .concertId(concertInfo.concertId())
                .concertTitle(concertInfo.concertTitle())
                .concertScheduleId(concertScheduleInfo.concertScheduleId())
                .playDateTime(concertScheduleInfo.playDateTime())
                .now(now)
                .seats(seatCreateCommands)
                .build();
    }

    @Getter
    public static class SeatCreateCommand {
        long seatId;
        int seatNum;
        SeatGrade seatGrade;
        int seatPrice;

        public SeatCreateCommand(long seatId, int seatNum, SeatGrade seatGrade, int seatPrice) {
            this.seatId = seatId;
            this.seatNum = seatNum;
            this.seatGrade = seatGrade;
            this.seatPrice = seatPrice;
        }
    }
}
