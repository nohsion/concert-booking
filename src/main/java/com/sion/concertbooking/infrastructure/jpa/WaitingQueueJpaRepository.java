package com.sion.concertbooking.infrastructure.jpa;

import com.sion.concertbooking.domain.watingqueue.WaitingQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaitingQueueJpaRepository extends JpaRepository<WaitingQueue, Long> {

    WaitingQueue findByTokenId(String tokenId);

    List<WaitingQueue> findByStatusOrderById(WaitingQueue.Status status);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE WaitingQueue w SET w.status = :status WHERE w.tokenId IN :tokens")
    int updateStatusInBatch(
            @Param("tokens") List<String> tokens,
            @Param("status") WaitingQueue.Status status
    );
}
