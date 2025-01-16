package com.sion.concertbooking.domain.reservation;

import com.sion.concertbooking.domain.BaseEntity;
import com.sion.concertbooking.domain.seat.SeatGrade;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "reservation")
public class Reservation extends BaseEntity {

    private static final int TEMPORARY_EXPIRE_MINUTES = 5;
    private static final int CANCEL_DEADLINE_DAYS_FROM_NOW = 7;
    private static final int CANCEL_DEADLINE_DAYS_BEFORE_PLAY = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(name = "concert_id", nullable = false)
    private long concertId;

    @Column(name = "concert_title")
    private String concertTitle;

    @Column(name = "concert_schedule_id")
    private Long concertScheduleId;

    @Column(name = "play_date")
    private LocalDateTime playDateTime;

    @Column(name = "cancel_deadline_date", nullable = false)
    private LocalDateTime cancelDeadlineDateTime;

    @Column(name = "seat_id", nullable = false)
    private long seatId;

    @Column(name = "seat_num")
    private Integer seatNum;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_grade")
    private SeatGrade seatGrade;

    @Column(name = "seat_price")
    private Integer seatPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReservationStatus status;

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;

    public static Reservation createReservation(
            long userId,
            long concertId,
            String concertTitle,
            Long concertScheduleId,
            LocalDateTime playDateTime,
            LocalDateTime now,
            Long seatId,
            Integer seatNum,
            SeatGrade seatGrade,
            Integer seatPrice
    ) {
        Reservation reservation = new Reservation();
        reservation.userId = userId;
        reservation.concertId = concertId;
        reservation.concertTitle = concertTitle;
        reservation.concertScheduleId = concertScheduleId;
        reservation.playDateTime = playDateTime;
        // 공연 취소 기한은 현재 시간으로부터 7일 후로 설정하되, 공연 시작 1일 전까지로 제한한다.
        LocalDateTime cancelDeadLineDateTime = now.plusDays(CANCEL_DEADLINE_DAYS_FROM_NOW);
        reservation.cancelDeadlineDateTime = cancelDeadLineDateTime.isBefore(playDateTime.minusDays(CANCEL_DEADLINE_DAYS_BEFORE_PLAY))
                ? cancelDeadLineDateTime
                : playDateTime.minusDays(CANCEL_DEADLINE_DAYS_BEFORE_PLAY);
        reservation.seatId = seatId;
        reservation.seatNum = seatNum;
        reservation.seatGrade = seatGrade;
        reservation.seatPrice = seatPrice;
        reservation.status = ReservationStatus.SUSPEND;
        reservation.expiredAt = now.plusMinutes(TEMPORARY_EXPIRE_MINUTES);

        return reservation;
    }

    public boolean isExpired(LocalDateTime now) {
        return expiredAt.isBefore(now);
    }

    public boolean isReserved() {
        return ReservationStatus.SUCCESS == status;
    }

    /**
     * 결제 대기 상태는 예약 만료 시간이 지나지 않았을 때만 가능하다.
     */
    public boolean isSuspend(LocalDateTime now) {
        return ReservationStatus.SUSPEND == status && !isExpired(now);
    }

    public void markSuccess() {
        this.status = ReservationStatus.SUCCESS;
    }
}
