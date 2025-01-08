package com.sion.concertbooking.presentation.rest;

import com.sion.concertbooking.application.PaymentFacade;
import com.sion.concertbooking.application.criteria.PaymentCriteria;
import com.sion.concertbooking.application.result.PaymentResult;
import com.sion.concertbooking.infrastructure.aspect.TokenInfo;
import com.sion.concertbooking.infrastructure.aspect.TokenRequired;
import com.sion.concertbooking.infrastructure.aspect.TokenUtils;
import com.sion.concertbooking.presentation.request.PaymentRequest;
import com.sion.concertbooking.presentation.response.PaymentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

import static com.sion.concertbooking.domain.service.TokenProvider.CONCERT_TOKEN_HEADER;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private static final String TAG_NAME = "payment";

    private final PaymentFacade paymentFacade;

    public PaymentController(PaymentFacade paymentFacade) {
        this.paymentFacade = paymentFacade;
    }

    @Operation(summary = "콘서트 좌석 결제", description = "콘서트 좌석들을 결제하고, 해당 유저의 대기열 토큰을 만료시킨다.",
            tags = {TAG_NAME}, security = {@SecurityRequirement(name = CONCERT_TOKEN_HEADER)})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "콘서트 좌석 결제 성공"),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰"),
    })
    @TokenRequired
    @PostMapping
    public ResponseEntity<PaymentResponse> processPayment(
            @RequestBody PaymentRequest paymentRequest
    ) throws AuthenticationException {
        TokenInfo tokenInfo = TokenUtils.getTokenInfo();

        PaymentCriteria paymentCriteria = new PaymentCriteria(
                tokenInfo.userId(), tokenInfo.tokenId(), paymentRequest.reservationIds()
        );

        PaymentResult paymentResult = paymentFacade.processPayment(paymentCriteria);

        return ResponseEntity.ok(PaymentResponse.fromResult(paymentResult));
    }
}
