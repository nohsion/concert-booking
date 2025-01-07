package com.sion.concertbooking.presentation.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sion.concertbooking.domain.model.info.ReservationInfo;
import com.sion.concertbooking.domain.enums.ReservationStatus;
import com.sion.concertbooking.domain.enums.SeatGrade;

import java.time.LocalDateTime;

public record ReservationResponse(
        @JsonProperty(value = "reservationId") long reservationId,
        @JsonProperty(value = "concertId") long concertId,
        @JsonProperty(value = "concertTitle") String concertTitle,
        @JsonProperty(value = "concertScheduleId") long concertScheduleId,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonProperty(value = "playDateTime") LocalDateTime playDateTime,
        @JsonProperty(value = "seatId") long seatId,
        @JsonProperty(value = "seatNum") int seatNum,
        @JsonProperty(value = "seatGrade") SeatGrade seatGrade,
        @JsonProperty(value = "seatPrice") int seatPrice,
        @JsonProperty(value = "reservationStatus") ReservationStatus reservationStatus
) {

    public static ReservationResponse fromDto(ReservationInfo reservationInfo) {
        return new ReservationResponse(
                reservationInfo.reservationId(),
                reservationInfo.concertId(),
                reservationInfo.concertTitle(),
                reservationInfo.concertScheduleId(),
                reservationInfo.playDateTime(),
                reservationInfo.seatId(),
                reservationInfo.seatNum(),
                reservationInfo.seatGrade(),
                reservationInfo.seatPrice(),
                reservationInfo.reservationStatus()
        );
    }
}
