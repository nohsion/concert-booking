package com.sion.concertbooking.presentation.rest;

import com.sion.concertbooking.domain.reservation.ReservationDto;
import com.sion.concertbooking.domain.reservation.ReservationStatus;
import com.sion.concertbooking.domain.seat.SeatGrade;
import com.sion.concertbooking.domain.userpoint.UserPointDto;
import com.sion.concertbooking.presentation.request.ReservationCreateRequest;
import com.sion.concertbooking.presentation.response.PaymentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private static final String TAG_NAME = "payment";

    @Operation(summary = "콘서트 좌석 결제", description = "콘서트 좌석들을 결제하고, 해당 유저의 대기열 토큰을 만료시킨다.",
            tags = {TAG_NAME})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "콘서트 좌석 결제 성공")
    })
    @PostMapping
    public ResponseEntity<PaymentResponse> processPayment(
            @RequestParam(value = "tokenId") String tokenId,
            @RequestBody ReservationCreateRequest reservationCreateRequest
    ) {
        long userId = 1L;
        int point = 1000;

        LocalDateTime dateTime = LocalDateTime.of(2025, 1, 3, 0, 0);

        PaymentResponse paymentResponse = new PaymentResponse(
                new UserPointDto(userId, point, dateTime),
                List.of(
                        new ReservationDto(1L, 1L, "지킬앤하이드", 1L, dateTime,
                                10L, 10, SeatGrade.VIP, 100_000, ReservationStatus.SUCCESS),
                        new ReservationDto(2L, 1L, "지킬앤하이드", 1L, dateTime,
                                11L, 11, SeatGrade.VIP, 100_000, ReservationStatus.SUCCESS)
                )
        );
        return ResponseEntity.ok(paymentResponse);
    }
}
