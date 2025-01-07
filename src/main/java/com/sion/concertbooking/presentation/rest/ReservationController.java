package com.sion.concertbooking.presentation.rest;

import com.sion.concertbooking.application.ConcertReservationFacade;
import com.sion.concertbooking.application.result.ReservationResult;
import com.sion.concertbooking.infrastructure.aspect.TokenInfo;
import com.sion.concertbooking.infrastructure.aspect.TokenRequired;
import com.sion.concertbooking.infrastructure.aspect.TokenUtils;
import com.sion.concertbooking.presentation.request.ConcertReservationCreateRequest;
import com.sion.concertbooking.presentation.response.ReservationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.List;

import static com.sion.concertbooking.domain.service.TokenProvider.CONCERT_TOKEN_HEADER;

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

        List<ReservationResult> reservationInfos = concertReservationFacade.reserve(userId, concertReservationCreateRequest);
        List<ReservationResponse> reservationResponses = reservationInfos.stream()
                .map(ReservationResponse::fromResult)
                .toList();

        return ResponseEntity.ok(reservationResponses);
    }


}
