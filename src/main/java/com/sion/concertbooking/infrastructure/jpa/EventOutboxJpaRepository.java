package com.sion.concertbooking.infrastructure.jpa;

import com.sion.concertbooking.domain.event.EventOutbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventOutboxJpaRepository extends JpaRepository<EventOutbox, Long> {
    List<EventOutbox> findByAggregateTypeAndEventTypeAndStatusAndCreatedAtBefore(
            String aggregateType, String eventType, EventOutbox.Status status, LocalDateTime createdAt
    );

    List<EventOutbox> findByAggregateTypeAndAggregateIdAndEventType(
            String aggregateType, long aggregateId, String eventType
    );
}
