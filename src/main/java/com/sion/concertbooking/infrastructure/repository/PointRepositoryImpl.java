package com.sion.concertbooking.infrastructure.repository;

import com.sion.concertbooking.domain.entity.Point;
import com.sion.concertbooking.domain.repository.PointRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PointRepositoryImpl implements PointRepository {

    private final PointJpaRepository pointJpaRepository;

    public PointRepositoryImpl(final PointJpaRepository pointJpaRepository) {
        this.pointJpaRepository = pointJpaRepository;
    }

    @Override
    public Optional<Point> findById(final long pointId) {
        return pointJpaRepository.findById(pointId);
    }

    @Override
    public Point findByUserId(final long userId) {
        return pointJpaRepository.findByUserId(userId);
    }

    @Override
    public Point findByUserIdWithLock(final long userId) {
        return pointJpaRepository.findByUserIdWithLock(userId);
    }

    @Override
    public Point save(final Point point) {
        return pointJpaRepository.save(point);
    }
}
