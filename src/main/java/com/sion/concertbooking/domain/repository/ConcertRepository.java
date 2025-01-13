package com.sion.concertbooking.domain.repository;

import com.sion.concertbooking.domain.entity.Concert;

import java.util.Optional;

public interface ConcertRepository {
    Optional<Concert> findById(long concertId);
}
