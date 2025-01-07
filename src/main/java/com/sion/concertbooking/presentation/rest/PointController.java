package com.sion.concertbooking.presentation.rest;

import com.sion.concertbooking.application.PaymentType;
import com.sion.concertbooking.application.PointCharger;
import com.sion.concertbooking.application.PointDeductor;
import com.sion.concertbooking.application.result.PointResult;
import com.sion.concertbooking.presentation.response.PointChargeResponse;
import com.sion.concertbooking.presentation.response.PointUseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 포인트 충전/사용/조회는 서비스 토큰이 없어도 가능하다.
 */
@RestController
@RequestMapping(value = "/api/v1/point")
public class PointController {

    private final PointCharger pointCharger;
    private final PointDeductor pointDeductor;

    public PointController(PointCharger pointCharger, PointDeductor pointDeductor) {
        this.pointCharger = pointCharger;
        this.pointDeductor = pointDeductor;
    }

    private static final String TAG_NAME = "amount";

    @Operation(summary = "포인트 충전", description = "결제수단에 맞게 유저의 포인트를 충전한다.",
            tags = {TAG_NAME})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포인트 충전 성공")
    })
    @PostMapping("/charge")
    public ResponseEntity<PointChargeResponse> chargePoint(
            @RequestParam(value = "userId") long userId,
            @RequestParam(value = "amount") int amount,
            @RequestParam(value = "paymentType", defaultValue = "FREE") String paymentType
    ) {
        PointResult.Charge pointChargeResult = pointCharger.chargePoint(userId, amount, PaymentType.valueOf(paymentType));

        return ResponseEntity.ok(PointChargeResponse.fromResult(pointChargeResult));
    }

    @Operation(summary = "포인트 사용", description = "유저의 포인트를 사용한다.",
            tags = {TAG_NAME})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포인트 사용 성공")
    })
    @PostMapping("/use")
    public ResponseEntity<PointUseResponse> usePoint(
            @RequestParam(value = "userId") long userId,
            @RequestParam(value = "amount") int amount
    ) {
        PointResult.Use pointUseResult = pointDeductor.usePoint(userId, amount);

        return ResponseEntity.ok(PointUseResponse.fromResult(pointUseResult));
    }
}
