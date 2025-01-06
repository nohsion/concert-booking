package com.sion.concertbooking.domain.dto;

import com.sion.concertbooking.domain.enums.SeatGrade;
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
public class ReservationCreateDto {
    private long userId;
    private long concertId;
    private String concertTitle;
    private long concertScheduleId;
    private LocalDateTime playDateTime;
    private LocalDateTime now;
    private List<SeatCreateDto> seats;

    @Getter
    public static class SeatCreateDto {
        long seatId;
        int seatNum;
        SeatGrade seatGrade;
        int seatPrice;

        public SeatCreateDto(long seatId, int seatNum, SeatGrade seatGrade, int seatPrice) {
            this.seatId = seatId;
            this.seatNum = seatNum;
            this.seatGrade = seatGrade;
            this.seatPrice = seatPrice;
        }
    }
}
