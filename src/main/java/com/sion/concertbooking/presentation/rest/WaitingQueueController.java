package com.sion.concertbooking.presentation.rest;

import com.sion.concertbooking.domain.dto.WaitingQueueDto;
import com.sion.concertbooking.domain.service.WaitingQueueService;
import com.sion.concertbooking.presentation.request.WaitingQueueRegisterRequest;
import com.sion.concertbooking.presentation.response.QueueResponse;
import com.sion.concertbooking.presentation.response.WaitingQueueRegisterResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/api/v1/waiting")
public class WaitingQueueController {

    private final WaitingQueueService waitingQueueService;

    public WaitingQueueController(
            WaitingQueueService waitingQueueService
    ) {
        this.waitingQueueService = waitingQueueService;
    }

    private static final String TAG_NAME = "waiting";

    @Operation(summary = "대기열 등록", description = "콘서트 예매 대기 줄을 세우고 예매 토큰을 발급한다.",
            tags = {TAG_NAME})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "대기열 등록 성공")
    })
    @PostMapping
    public ResponseEntity<WaitingQueueRegisterResponse> waitQueueAndIssueToken(
            @RequestBody WaitingQueueRegisterRequest waitingQueueRegisterRequest
    ) {
        WaitingQueueDto waitingQueueDto = waitingQueueService.waitQueueAndIssueToken(
                waitingQueueRegisterRequest.userId(),
                waitingQueueRegisterRequest.concertId(),
                LocalDateTime.now()
        );
        return ResponseEntity.ok(WaitingQueueRegisterResponse.fromDto(waitingQueueDto));
    }

    @Operation(summary = "대기열 정보 조회", description = "토큰을 통해 대기열 정보를 조회한다.",
            tags = {TAG_NAME})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "대기열 정보 조회 성공")
    })
    @GetMapping
    public ResponseEntity<QueueResponse> getQueueByToken(
            @RequestParam(value = "tokenId") String tokenId
    ) {
        QueueResponse queueResponse = new QueueResponse(tokenId, 10, 50);
        return ResponseEntity.ok(queueResponse);
    }
}
