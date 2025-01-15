package com.sion.concertbooking.domain.point;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

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
            throw new NoSuchElementException("해당 유저는 포인트 지갑을 가지고 있지 않습니다. userId=" + userId);
        }
        point.chargePoint(amount);
        return PointInfo.fromEntity(point);
    }

    @Transactional
    public PointInfo usePoint(long userId, int amount) {
        Point point = pointRepository.findByUserIdWithLock(userId);
        if (point == null) {
            throw new NoSuchElementException("해당 유저는 포인트 지갑을 가지고 있지 않습니다. userId=" + userId);
        }
        point.usePoint(amount);
        return PointInfo.fromEntity(point);
    }

    public PointInfo getPointById(long pointId) {
        Point point = pointRepository.findById(pointId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 포인트입니다. pointId=" + pointId));
        return PointInfo.fromEntity(point);
    }

    public PointInfo getPointByUserId(long userId) {
        Point point = pointRepository.findByUserId(userId);
        if (point == null) {
            throw new NoSuchElementException("해당 유저는 포인트 지갑을 가지고 있지 않습니다. userId=" + userId);
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
