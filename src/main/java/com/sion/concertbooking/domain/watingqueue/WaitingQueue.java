package com.sion.concertbooking.domain.watingqueue;

import com.sion.concertbooking.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "waiting_queue")
public class WaitingQueue extends BaseEntity {

    private static final int EXPIRED_MINUTES = 10;
    private static final List<WaitingQueueStatus> VALID_STATUSES = List.of(
            WaitingQueueStatus.WAITING, WaitingQueueStatus.ENTERED
    );

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

    public void updateStatusExpired() {
        this.status = WaitingQueueStatus.EXPIRED;
    }

    public boolean isExpiredTime(LocalDateTime now) {
        return this.expiredAt.isBefore(now);
    }

    public boolean isTokenValid(LocalDateTime now) {
        return !isExpiredTime(now) && VALID_STATUSES.contains(this.status);
    }

    public boolean isProcessing(LocalDateTime now) {
        return !isExpiredTime(now) && this.status == WaitingQueueStatus.ENTERED;
    }
}
