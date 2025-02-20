package com.sion.concertbooking.domain.event;

import java.util.Optional;

public interface EventOutboxRepository {
    Optional<EventOutbox> findById(long eventId);

    EventOutbox save(EventOutbox eventOutbox);

    EventOutbox findByStatus(EventOutbox.Status status);

    EventOutbox findByAggregateTypeAndAggregateIdAndEventType(
            String aggregateType, long aggregateId, String eventType);
}
