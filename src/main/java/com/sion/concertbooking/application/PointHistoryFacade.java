package com.sion.concertbooking.application;

import com.sion.concertbooking.application.criteria.PointHistoryCriteria;
import com.sion.concertbooking.application.result.PointHistoryResult;
import com.sion.concertbooking.domain.info.PointInfo;
import com.sion.concertbooking.domain.service.PointHistoryService;
import com.sion.concertbooking.domain.service.PointService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class PointHistoryFacade {

    private final PointHistoryService pointHistoryService;
    private final PointService pointService;

    public PointHistoryFacade(
            PointHistoryService pointHistoryService,
            PointService pointService
    ) {
        this.pointHistoryService = pointHistoryService;
        this.pointService = pointService;
    }

    public Page<PointHistoryResult> getUserPointHistories(PointHistoryCriteria criteria, Pageable pageable) {
        long userId = criteria.userId();
        PointInfo pointInfo = pointService.getPointByUserId(userId);
        long pointId = pointInfo.id();

        return switch (criteria.transactionType()) {
            case CHARGE -> pointHistoryService.getChargedPointHistories(pointId, pageable)
                    .map(PointHistoryResult::fromInfo);
            case USE -> pointHistoryService.getUsedPointHistories(pointId, pageable)
                    .map(PointHistoryResult::fromInfo);
            case null -> pointHistoryService.getPointHistories(pointId, pageable)
                    .map(PointHistoryResult::fromInfo);
        };
    }
}
