package com.sion.concertbooking.intefaces.presentation.rest;

import com.sion.concertbooking.application.concert.ConcertFacade;
import com.sion.concertbooking.application.concert.ConcertScheduleResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/concert")
public class ConcertController {

    private static final String TAG_NAME = "concert";

    private final ConcertFacade concertFacade;

    public ConcertController(ConcertFacade concertFacade) {
        this.concertFacade = concertFacade;
    }

    @Operation(summary = "콘서트 스케쥴 목록 조회", description = "특정 콘서트의 스케쥴 목록을 조회한다.",
            tags = {TAG_NAME})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "콘서트 스케쥴 목록 조회 성공"),
    })
    @GetMapping("/schedules")
    public ResponseEntity<List<ConcertScheduleResult>> getConcertSchedules(
            @RequestParam long concertId
    ) {
        List<ConcertScheduleResult> concertSchedules = concertFacade.getConcertSchedules(concertId);
        return ResponseEntity.ok(concertSchedules);
    }
}
