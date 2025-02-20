package com.sion.concertbooking.domain.event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventOutboxRepository {
    Optional<EventOutbox> findById(long eventId);

    EventOutbox save(EventOutbox eventOutbox);

    List<EventOutbox> findByAggregateTypeAndEventTypeAndStatusAndCreatedAtBefore(
            String aggregateType, String eventType, EventOutbox.Status status, LocalDateTime createdAt
    );

    List<EventOutbox> findByAggregateTypeAndAggregateIdAndEventType(
            String aggregateType, long aggregateId, String eventType);
}
