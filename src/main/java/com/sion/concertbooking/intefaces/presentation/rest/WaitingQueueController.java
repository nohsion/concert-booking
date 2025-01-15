package com.sion.concertbooking.intefaces.presentation.rest;

import com.sion.concertbooking.domain.watingqueue.WaitingQueueInfo;
import com.sion.concertbooking.domain.watingqueue.WaitingQueueDetailInfo;
import com.sion.concertbooking.domain.watingqueue.WaitingQueueService;
import com.sion.concertbooking.intefaces.aspect.TokenInfo;
import com.sion.concertbooking.intefaces.aspect.TokenRequired;
import com.sion.concertbooking.intefaces.aspect.TokenUtils;
import com.sion.concertbooking.intefaces.presentation.request.WaitingQueueRegisterRequest;
import com.sion.concertbooking.intefaces.presentation.response.WaitingQueueInfoResponse;
import com.sion.concertbooking.intefaces.presentation.response.WaitingQueueRegisterResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.time.LocalDateTime;

import static com.sion.concertbooking.domain.token.TokenProvider.CONCERT_TOKEN_HEADER;

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
        WaitingQueueInfo waitingQueueInfo = waitingQueueService.waitQueueAndIssueToken(
                waitingQueueRegisterRequest.userId(),
                waitingQueueRegisterRequest.concertId(),
                LocalDateTime.now()
        );
        return ResponseEntity.ok(WaitingQueueRegisterResponse.fromDto(waitingQueueInfo));
    }

    @Operation(summary = "대기열 정보 조회", description = "토큰을 통해 대기열 정보를 조회한다.",
            tags = {TAG_NAME}, security = {@SecurityRequirement(name = CONCERT_TOKEN_HEADER)})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "대기열 정보 조회 성공"),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰")
    })
    @TokenRequired
    @GetMapping
    public ResponseEntity<WaitingQueueInfoResponse> getQueueByToken() throws AuthenticationException {
        TokenInfo tokenInfo = TokenUtils.getTokenInfo();
        String tokenId = tokenInfo.tokenId();
        WaitingQueueDetailInfo waitingQueueDetailInfo =
                waitingQueueService.getQueueDetailByTokenId(tokenId, LocalDateTime.now());
        return ResponseEntity.ok(WaitingQueueInfoResponse.fromInfo(waitingQueueDetailInfo));
    }
}
