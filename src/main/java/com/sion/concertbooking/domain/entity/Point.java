package com.sion.concertbooking.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "point")
public class Point extends BaseEntity {

    private static final long USER_MAX_POINT = 100_000_000L;
    private static final int ONCE_CHARGE_MAX_POINT = 2_000_000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(name = "amount", nullable = false)
    private int amount;

    public void chargePoint(int point) {
        if (point <= 0) {
            throw new IllegalArgumentException("0 이하의 금액을 충전할 수 없습니다.");
        }
        if (point > ONCE_CHARGE_MAX_POINT) {
            throw new IllegalArgumentException("한번에 충전 가능한 금액을 초과했습니다.");
        }
        int amountToSave = amount + point;
        if (amountToSave > USER_MAX_POINT) {
            throw new IllegalArgumentException("최대 잔고를 초과하여 충전할 수 없습니다.");
        }
        this.amount = amountToSave;
    }

    public void usePoint(int point) {
        if (point <= 0) {
            throw new IllegalArgumentException("0 이하의 금액을 사용할 수 없습니다.");
        }
        int amountToSave = amount - point;
        if (amountToSave < 0) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }
        this.amount = amountToSave;
    }
}
