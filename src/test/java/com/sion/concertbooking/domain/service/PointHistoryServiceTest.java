package com.sion.concertbooking.domain.service;

import com.sion.concertbooking.domain.pointhistory.PointHistory;
import com.sion.concertbooking.domain.pointhistory.TransactionType;
import com.sion.concertbooking.domain.pointhistory.PointHistoryInfo;
import com.sion.concertbooking.domain.pointhistory.PointHistoryService;
import com.sion.concertbooking.domain.pointhistory.PointHistoryRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PointHistoryServiceTest {

    private PointHistoryService sut;

    private PointHistoryRepository pointHistoryRepository = mock(PointHistoryRepository.class);

    @BeforeEach
    void setUp() {
        sut = new PointHistoryService(pointHistoryRepository);
    }

    @DisplayName("포인트 충전 내역만 페이징 조회한다.")
    @Test
    void getChargedPointHistories() {
        // given
        long pointId = 1L;
        TransactionType transactionType = TransactionType.CHARGE;
        int pageSize = 10;
        long totalCount = 135L;
        Pageable pageable = PageRequest.of(0, pageSize);

        List<PointHistory> pointHistories = Instancio.ofList(PointHistory.class).size(10)
                .create();
        Page<PointHistory> pagedPointHistories = new PageImpl<>(
                pointHistories, pageable, totalCount
        );

        when(pointHistoryRepository.findByPointIdAndType(pointId, transactionType, pageable))
                .thenReturn(pagedPointHistories);

        // when
        Page<PointHistoryInfo> result = sut.getChargedPointHistories(pointId, pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(totalCount);
        assertThat(result.getTotalPages()).isEqualTo(14);
        assertThat(result.getContent())
                .usingRecursiveComparison().isEqualTo(
                        pagedPointHistories.map(PointHistoryInfo::fromEntity).getContent());
    }

    @DisplayName("포인트 사용 내역만 페이징 조회한다.")
    @Test
    void getUsedPointHistories() {
        // given
        long pointId = 1L;
        TransactionType transactionType = TransactionType.USE;
        int pageSize = 10;
        long totalCount = 135L;
        Pageable pageable = PageRequest.of(0, pageSize);

        List<PointHistory> pointHistories = Instancio.ofList(PointHistory.class).size(10)
                .create();
        Page<PointHistory> pagedPointHistories = new PageImpl<>(
                pointHistories, pageable, totalCount
        );

        when(pointHistoryRepository.findByPointIdAndType(pointId, transactionType, pageable))
                .thenReturn(pagedPointHistories);

        // when
        Page<PointHistoryInfo> result = sut.getUsedPointHistories(pointId, pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(totalCount);
        assertThat(result.getTotalPages()).isEqualTo(14);
        assertThat(result.getContent())
                .usingRecursiveComparison().isEqualTo(
                        pagedPointHistories.map(PointHistoryInfo::fromEntity).getContent());
    }

    @DisplayName("포인트 내역을 모두 페이징 조회한다.")
    @Test
    void getPointHistories() {
        // given
        long pointId = 1L;
        int pageSize = 10;
        long totalCount = 135L;
        Pageable pageable = PageRequest.of(0, pageSize);

        List<PointHistory> pointHistories = Instancio.ofList(PointHistory.class).size(10)
                .create();
        Page<PointHistory> pagedPointHistories = new PageImpl<>(
                pointHistories, pageable, totalCount
        );

        when(pointHistoryRepository.findByPointId(pointId, pageable))
                .thenReturn(pagedPointHistories);

        // when
        Page<PointHistoryInfo> result = sut.getPointHistories(pointId, pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(totalCount);
        assertThat(result.getTotalPages()).isEqualTo(14);
        assertThat(result.getContent())
                .usingRecursiveComparison().isEqualTo(
                        pagedPointHistories.map(PointHistoryInfo::fromEntity).getContent());
    }

}