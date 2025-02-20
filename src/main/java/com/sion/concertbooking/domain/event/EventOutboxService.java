package com.sion.concertbooking.domain.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Component
public class EventOutboxService {

    private final EventOutboxRepository eventOutboxRepository;

    public EventOutboxService(EventOutboxRepository eventOutboxRepository) {
        this.eventOutboxRepository = eventOutboxRepository;
    }

    public EventOutbox save(EventOutbox eventOutbox) {
        return eventOutboxRepository.save(eventOutbox);
    }

    public EventOutbox findByStatus(EventOutbox.Status status) {
        return eventOutboxRepository.findByStatus(status);
    }

    public EventOutboxInfo getEventOutBox(EventOutboxCommand command) {
        EventOutbox eventOutbox = eventOutboxRepository.findByAggregateTypeAndAggregateIdAndEventType(
                command.aggregateType(),
                command.aggregateId(),
                command.eventType()
        );
        return EventOutboxInfo.fromEntity(eventOutbox);
    }

    @Transactional
    public void done(long eventId) {
        EventOutbox eventOutbox = eventOutboxRepository.findById(eventId)
                .orElseThrow(() -> new NoSuchElementException("EventOutbox not found. id=" + eventId));
        eventOutbox.done();
    }
}
