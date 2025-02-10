package com.sion.concertbooking.intefaces.presentation.rest;

import com.sion.concertbooking.application.payment.PaymentFacade;
import com.sion.concertbooking.application.payment.PaymentCriteria;
import com.sion.concertbooking.application.payment.PaymentResult;
import com.sion.concertbooking.domain.waitingtoken.WaitingTokenInfo;
import com.sion.concertbooking.intefaces.aspect.TokenRequired;
import com.sion.concertbooking.intefaces.aspect.TokenUtils;
import com.sion.concertbooking.intefaces.presentation.accesslog.LogGroup;
import com.sion.concertbooking.intefaces.presentation.accesslog.LogMapping;
import com.sion.concertbooking.intefaces.presentation.request.PaymentRequest;
import com.sion.concertbooking.intefaces.presentation.response.PaymentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.sion.concertbooking.domain.waitingtoken.TokenProvider.CONCERT_TOKEN_HEADER;

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
    @LogMapping(logGroup = LogGroup.PAYMENT)
    @PostMapping
    public ResponseEntity<PaymentResponse> processPayment(
            @RequestBody PaymentRequest paymentRequest
    ) {
        WaitingTokenInfo tokenInfo = TokenUtils.getTokenInfo();

        PaymentCriteria paymentCriteria = new PaymentCriteria(
                tokenInfo.userId(),
                tokenInfo.tokenId(),
                paymentRequest.reservationIds(),
                paymentRequest.concertId()
        );

        PaymentResult paymentResult = paymentFacade.processPayment(paymentCriteria);

        return ResponseEntity.ok(PaymentResponse.fromResult(paymentResult));
    }
}
