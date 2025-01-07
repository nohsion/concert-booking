package com.sion.concertbooking.domain.entity;

import com.sion.concertbooking.domain.enums.WaitingQueueStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "waiting_queue")
public class WaitingQueue extends BaseEntity {

    private static final int EXPIRED_MINUTES = 10;

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

    public static WaitingQueue of(String tokenId, long userId, long concertId, LocalDateTime now) {
        WaitingQueue waitingQueue = new WaitingQueue();
        waitingQueue.tokenId = tokenId;
        waitingQueue.userId = userId;
        waitingQueue.concertId = concertId;
        waitingQueue.status = WaitingQueueStatus.WAITING;
        waitingQueue.expiredAt = now.plusMinutes(EXPIRED_MINUTES);
        return waitingQueue;
    }

    public boolean isExpired(LocalDateTime now) {
        return expiredAt.isBefore(now);
    }

    public void markExpired() {
        this.status = WaitingQueueStatus.EXPIRED;
    }

    public boolean isTokenValid(LocalDateTime now) {
        return this.status == WaitingQueueStatus.ENTERED && !isExpired(now);
    }
}
