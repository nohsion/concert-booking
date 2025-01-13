package com.sion.concertbooking.domain.service;

import com.sion.concertbooking.domain.entity.Point;
import com.sion.concertbooking.domain.info.PointInfo;
import com.sion.concertbooking.domain.repository.PointRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PointService {

    private final PointRepository pointRepository;

    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    @Transactional
    public PointInfo chargePoint(long userId, int amount) {
        Point point = pointRepository.findByUserIdWithLock(userId);
        if (point == null) {
            throw new IllegalArgumentException("존재하지 않는 포인트입니다.");
        }
        point.chargePoint(amount);
        return PointInfo.fromEntity(point);
    }

    @Transactional
    public PointInfo usePoint(long userId, int amount) {
        Point point = pointRepository.findByUserIdWithLock(userId);
        if (point == null) {
            throw new IllegalArgumentException("존재하지 않는 포인트입니다.");
        }
        point.usePoint(amount);
        return PointInfo.fromEntity(point);
    }

    public PointInfo getPointById(long pointId) {
        Point point = pointRepository.findById(pointId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 포인트입니다."));
        return PointInfo.fromEntity(point);
    }

    public PointInfo getPointByUserId(long userId) {
        Point point = pointRepository.findByUserId(userId);
        if (point == null) {
            throw new IllegalArgumentException("존재하지 않는 포인트입니다.");
        }
        return PointInfo.fromEntity(point);
    }

    @Transactional
    public PointInfo createPoint(long userId) {
        Point pointWallet = Point.createWallet(userId);
        Point savedPoint = pointRepository.save(pointWallet);
        return PointInfo.fromEntity(savedPoint);
    }

}
