package com.sion.concertbooking.intefaces.presentation.rest;

import com.sion.concertbooking.application.reservation.ConcertReservationFacade;
import com.sion.concertbooking.application.reservation.ReservationCriteria;
import com.sion.concertbooking.application.reservation.ReservationResult;
import com.sion.concertbooking.intefaces.aspect.TokenInfo;
import com.sion.concertbooking.intefaces.aspect.TokenRequired;
import com.sion.concertbooking.intefaces.aspect.TokenUtils;
import com.sion.concertbooking.intefaces.presentation.request.ConcertReservationCreateRequest;
import com.sion.concertbooking.intefaces.presentation.response.ReservationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.List;

import static com.sion.concertbooking.domain.token.TokenProvider.CONCERT_TOKEN_HEADER;

@RestController
@RequestMapping(value = "/api/v1/reservation")
public class ReservationController {

    private static final String TAG_NAME = "reservation";

    private final ConcertReservationFacade concertReservationFacade;

    public ReservationController(ConcertReservationFacade concertReservationFacade) {
        this.concertReservationFacade = concertReservationFacade;
    }

    @Operation(summary = "콘서트 좌석 예약", description = "콘서트 좌석들을 예약한다. 모두 빈 좌석이라면 5분동안 해당 좌석들을 선점한다.",
            tags = {TAG_NAME}, security = {@SecurityRequirement(name = CONCERT_TOKEN_HEADER)})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "콘서트 좌석 예약 성공"),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰"),
    })
    @TokenRequired
    @PostMapping
    public ResponseEntity<List<ReservationResponse>> createReservations(
            @RequestBody ConcertReservationCreateRequest concertReservationCreateRequest
    ) throws AuthenticationException {
        TokenInfo tokenInfo = TokenUtils.getTokenInfo();
        long userId = tokenInfo.userId();

        ReservationCriteria reservationCriteria = new ReservationCriteria(
                userId,
                concertReservationCreateRequest.concertId(),
                concertReservationCreateRequest.concertScheduleId(),
                concertReservationCreateRequest.seatIds()
        );

        List<ReservationResult> reservationInfos = concertReservationFacade.reserve(reservationCriteria);
        List<ReservationResponse> reservationResponses = reservationInfos.stream()
                .map(ReservationResponse::fromResult)
                .toList();

        return ResponseEntity.ok(reservationResponses);
    }


}
