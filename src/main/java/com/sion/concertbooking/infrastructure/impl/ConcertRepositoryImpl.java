package com.sion.concertbooking.infrastructure.impl;

import com.sion.concertbooking.domain.concert.Concert;
import com.sion.concertbooking.domain.concert.ConcertRepository;
import com.sion.concertbooking.infrastructure.jpa.ConcertJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertJpaRepository concertJpaRepository;

    public ConcertRepositoryImpl(final ConcertJpaRepository concertJpaRepository) {
        this.concertJpaRepository = concertJpaRepository;
    }

    @Override
    public Optional<Concert> findById(final long concertId) {
        return concertJpaRepository.findById(concertId);
    }

    @Override
    public Concert save(final Concert concert) {
        return concertJpaRepository.save(concert);
    }
}
