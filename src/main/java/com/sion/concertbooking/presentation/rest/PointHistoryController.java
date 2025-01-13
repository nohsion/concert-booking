package com.sion.concertbooking.presentation.rest;

import com.sion.concertbooking.application.PointHistoryFacade;
import com.sion.concertbooking.application.criteria.PointHistoryCriteria;
import com.sion.concertbooking.application.result.PointHistoryResult;
import com.sion.concertbooking.domain.pointhistory.TransactionType;
import com.sion.concertbooking.presentation.response.PointHistoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 포인트 내역은 서비스 토큰이 없어도 접근 가능하다.
 */
@RestController
@RequestMapping("/api/v1/point-history")
public class PointHistoryController {

    private final PointHistoryFacade pointHistoryFacade;

    public PointHistoryController(PointHistoryFacade pointHistoryFacade) {
        this.pointHistoryFacade = pointHistoryFacade;
    }

    private static final String TAG_NAME = "point-history";

    @Operation(summary = "포인트 충전/사용 내역 조회", description = "유저의 포인트 충전/사용 내역을 내림차순으로 조회한다.",
            tags = {TAG_NAME})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포인트 충전/사용 내역 조회 성공")
    })
    @GetMapping
    public ResponseEntity<List<PointHistoryResponse>> getPointHistory(
            @RequestParam(value = "userId") long userId,
            @RequestParam(value = "transactionType", required = false) String transactionTypeStr,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        TransactionType transactionType;
        try {
            transactionType = TransactionType.valueOf(transactionTypeStr);
        } catch (NullPointerException e) {
            transactionType = null;
        }
        PointHistoryCriteria pointHistoryCriteria = new PointHistoryCriteria(userId, transactionType);

        Page<PointHistoryResult> pointHistoryResults
                = pointHistoryFacade.getUserPointHistories(pointHistoryCriteria, pageable);

        List<PointHistoryResponse> pointHistoryResponses = pointHistoryResults.stream()
                .map(PointHistoryResponse::fromResult)
                .toList();
        return ResponseEntity.ok(pointHistoryResponses);
    }
}
