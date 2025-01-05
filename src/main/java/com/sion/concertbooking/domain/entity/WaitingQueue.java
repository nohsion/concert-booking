package com.sion.concertbooking.domain.entity;

import com.sion.concertbooking.domain.enums.WaitingQueueStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "waiting_queue")
public class WaitingQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "token_id", nullable = false)
    private String tokenId;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(name = "concert_id", nullable = false)
    private long concertId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private WaitingQueueStatus status;

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;

}
