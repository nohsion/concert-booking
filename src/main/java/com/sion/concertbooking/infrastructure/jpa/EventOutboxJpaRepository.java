package com.sion.concertbooking.infrastructure.jpa;

import com.sion.concertbooking.domain.event.EventOutbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventOutboxJpaRepository extends JpaRepository<EventOutbox, Long> {
    EventOutbox findByStatus(EventOutbox.Status status);

    EventOutbox findByAggregateTypeAndAggregateIdAndEventType(
            String aggregateType, long aggregateId, String eventType
    );
}
