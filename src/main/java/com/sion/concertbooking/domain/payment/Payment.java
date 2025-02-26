package com.sion.concertbooking.domain.payment;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(name = "amount", nullable = false)
    private long amount;

    private Payment(long userId, long amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public static Payment of(long userId, long amount) {
        return new Payment(userId, amount);
    }
}
