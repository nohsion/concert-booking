package com.sion.concertbooking.presentation.rest;

import com.sion.concertbooking.presentation.response.UserPointResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/api/v1/user-point")
public class UserPointController {

    private static final String TAG_NAME = "user-point";

    @Operation(summary = "포인트 충전", description = "유저의 포인트를 충전한다.",
            tags = {TAG_NAME})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포인트 충전 성공")
    })
    @PostMapping("/charge")
    public ResponseEntity<UserPointResponse> chargePoint(
            @RequestParam(value = "tokenId") String tokenId,
            @RequestParam(value = "amount") int amount
    ) {
        long userId = 1L;
        int point = 1000;
        LocalDateTime updatedAt = LocalDateTime.of(2025, 1, 3, 0, 0);
        UserPointResponse userPointResponse = new UserPointResponse(userId, point, updatedAt);
        return ResponseEntity.ok(userPointResponse);
    }

    @Operation(summary = "포인트 사용", description = "유저의 포인트를 사용한다.",
            tags = {TAG_NAME})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포인트 사용 성공")
    })
    @PostMapping("/use")
    public ResponseEntity<UserPointResponse> usePoint(
            @RequestParam(value = "tokenId") String tokenId,
            @RequestParam(value = "amount") int amount
    ) {
        long userId = 1L;
        int point = 1000;
        LocalDateTime updatedAt = LocalDateTime.of(2025, 1, 3, 0, 0);
        UserPointResponse userPointResponse = new UserPointResponse(userId, point, updatedAt);
        return ResponseEntity.ok(userPointResponse);
    }
}
