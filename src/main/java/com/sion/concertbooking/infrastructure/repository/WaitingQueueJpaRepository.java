package com.sion.concertbooking.infrastructure.repository;

import com.sion.concertbooking.domain.entity.WaitingQueue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitingQueueJpaRepository extends JpaRepository<WaitingQueue, Long> {
}
