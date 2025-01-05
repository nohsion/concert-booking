package com.sion.concertbooking.presentation.rest;

import com.sion.concertbooking.domain.enums.ReservationStatus;
import com.sion.concertbooking.domain.enums.SeatGrade;
import com.sion.concertbooking.presentation.request.ReservationCreateRequest;
import com.sion.concertbooking.presentation.response.ReservationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/reservation")
public class ReservationController {

    private static final String TAG_NAME = "reservation";

    @Operation(summary = "콘서트 좌석 예약", description = "콘서트 좌석들을 예약한다. 모두 빈 좌석이라면 5분동안 해당 좌석들을 선점한다.",
    tags = {TAG_NAME})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "콘서트 좌석 예약 성공")
    })
    @PostMapping
    public ResponseEntity<List<ReservationResponse>> createReservations(
            @RequestParam(value = "tokenId") String tokenId,
            @RequestBody ReservationCreateRequest reservationCreateRequest
    ) {
        LocalDateTime playDateTime = LocalDateTime.of(2025, 1, 3, 0, 0);
        List<ReservationResponse> reservationResponses = List.of(
                new ReservationResponse(
                        1L, 1L, "아리아나그란데 내한", 1L, playDateTime,
                        10L, 10, SeatGrade.VIP, 100_000, ReservationStatus.SUSPEND
                ),
                new ReservationResponse(
                        2L, 1L, "아리아나그란데 내한", 1L, playDateTime,
                        11L, 11, SeatGrade.VIP, 100_000, ReservationStatus.SUSPEND
                )
        );
        return ResponseEntity.ok(reservationResponses);
    }


}
