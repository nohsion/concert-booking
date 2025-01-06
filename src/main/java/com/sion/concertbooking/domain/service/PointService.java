package com.sion.concertbooking.domain.service;

import com.sion.concertbooking.domain.dto.PointDto;
import com.sion.concertbooking.domain.entity.Point;
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
    public PointDto chargePoint(long userId, int amount) {
        Point point = pointRepository.findByUserId(userId);
        if (point == null) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
        point.chargePoint(amount);
        return PointDto.fromEntity(point);
    }

    @Transactional
    public PointDto usePoint(long userId, int amount) {
        Point point = pointRepository.findByUserId(userId);
        if (point == null) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
        point.usePoint(amount);
        return PointDto.fromEntity(point);
    }

}
