package com.sion.concertbooking.infrastructure.repository;

import com.sion.concertbooking.domain.concert.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertJpaRepository extends JpaRepository<Concert, Long> {
}
