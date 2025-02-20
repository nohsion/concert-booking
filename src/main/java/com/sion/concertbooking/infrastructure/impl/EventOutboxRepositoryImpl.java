package com.sion.concertbooking.infrastructure.impl;

import com.sion.concertbooking.domain.event.EventOutbox;
import com.sion.concertbooking.domain.event.EventOutboxRepository;
import com.sion.concertbooking.infrastructure.jpa.EventOutboxJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class EventOutboxRepositoryImpl implements EventOutboxRepository {

    private final EventOutboxJpaRepository eventOutboxJpaRepository;

    public EventOutboxRepositoryImpl(EventOutboxJpaRepository eventOutboxJpaRepository) {
        this.eventOutboxJpaRepository = eventOutboxJpaRepository;
    }

    @Override
    public Optional<EventOutbox> findById(final long eventId) {
        return eventOutboxJpaRepository.findById(eventId);
    }

    @Override
    public EventOutbox save(EventOutbox eventOutbox) {
        return eventOutboxJpaRepository.save(eventOutbox);
    }

    @Override
    public List<EventOutbox> findByAggregateTypeAndEventTypeAndStatusAndCreatedAtBefore(
            String aggregateType, String eventType, EventOutbox.Status status, LocalDateTime createdAt
    ) {
        return eventOutboxJpaRepository.findByAggregateTypeAndEventTypeAndStatusAndCreatedAtBefore(
                aggregateType, eventType, status, createdAt);
    }

    @Override
    public List<EventOutbox> findByAggregateTypeAndAggregateIdAndEventType(
            String aggregateType, long aggregateId, String eventType
    ) {
        return eventOutboxJpaRepository.findByAggregateTypeAndAggregateIdAndEventType(
                aggregateType, aggregateId, eventType);
    }
}
