package com.sion.concertbooking.application.criteria;

import com.sion.concertbooking.domain.enums.TransactionType;
import jakarta.annotation.Nullable;

public record PointHistoryCriteria(
        long userId,
        @Nullable TransactionType transactionType
) {
}
