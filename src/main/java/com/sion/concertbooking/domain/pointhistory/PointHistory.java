package com.sion.concertbooking.domain.pointhistory;

import com.sion.concertbooking.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "point_history")
public class PointHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "point_id", nullable = false)
    private long pointId;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType type;

    public static PointHistory ofCharge(long pointId, int amount) {
        PointHistory pointHistory = new PointHistory();
        pointHistory.pointId = pointId;
        pointHistory.amount = amount;
        pointHistory.type = TransactionType.CHARGE;
        return pointHistory;
    }

    public static PointHistory ofUse(long pointId, int amount) {
        PointHistory pointHistory = new PointHistory();
        pointHistory.pointId = pointId;
        pointHistory.amount = amount;
        pointHistory.type = TransactionType.USE;
        return pointHistory;
    }
}
