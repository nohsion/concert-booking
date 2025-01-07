package com.sion.concertbooking.domain.model.entity;

import com.sion.concertbooking.domain.enums.SeatGrade;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "seat")
public class Seat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "theatre_id", nullable = false)
    private long theatreId;

    @Column(name = "seat_num")
    private int seatNum;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_grade")
    private SeatGrade seatGrade;

    @Column(name = "seat_price")
    private int seatPrice;

}
