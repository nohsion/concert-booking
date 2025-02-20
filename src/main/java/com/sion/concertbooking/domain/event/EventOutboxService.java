package com.sion.concertbooking.domain.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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

    public List<EventOutbox> getPendingOutboxes(String aggregateType, String eventType) {
        return eventOutboxRepository.findByAggregateTypeAndEventTypeAndStatusAndCreatedAtBefore(
                aggregateType,
                eventType,
                EventOutbox.Status.READY,
                LocalDateTime.now().minusMinutes(5)
        );
    }

    public List<EventOutboxInfo> getEventOutBoxes(EventOutboxCommand command) {
        List<EventOutbox> eventOutboxes = eventOutboxRepository.findByAggregateTypeAndAggregateIdAndEventType(
                command.aggregateType(),
                command.aggregateId(),
                command.eventType()
        );
        return eventOutboxes.stream()
                .map(EventOutboxInfo::fromEntity)
                .toList();
    }

    @Transactional
    public void done(long eventId) {
        EventOutbox eventOutbox = eventOutboxRepository.findById(eventId)
                .orElseThrow(() -> new NoSuchElementException("EventOutbox not found. id=" + eventId));
        eventOutbox.done();
    }
}
