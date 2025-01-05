package com.sion.concertbooking.domain.entity;

import com.sion.concertbooking.domain.enums.ReservationStatus;
import com.sion.concertbooking.domain.enums.SeatGrade;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "reservation")
public class Reservation {

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

    @Column(name = "play_date")
    private LocalDateTime playDateTime;

    @Column(name = "cancel_deadline_date", nullable = false)
    private LocalDateTime cancelDeadlineDateTime;

    @Column(name = "seat_id", nullable = false)
    private long seatId;

    @Column(name = "seat_num")
    private int seatNum;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_grade")
    private SeatGrade seatGrade;

    @Column(name = "seat_price")
    private int seatPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReservationStatus status;
}
