package com.sion.concertbooking.domain.concert;

import java.util.Optional;

public interface ConcertRepository {
    Optional<Concert> findById(long concertId);
    Concert save(Concert concert);
}
