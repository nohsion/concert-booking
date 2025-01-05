package com.sion.concertbooking.infrastructure.repository;

import com.sion.concertbooking.domain.entity.WaitingQueue;
import com.sion.concertbooking.domain.enums.WaitingQueueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WaitingQueueJpaRepository extends JpaRepository<WaitingQueue, Long> {

    WaitingQueue findByTokenId(String tokenId);

    List<WaitingQueue> findByStatusAndExpiredAtBeforeOrderByIdAsc(WaitingQueueStatus status, LocalDateTime now);
}
