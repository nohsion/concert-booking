package com.sion.concertbooking.infrastructure.repository;

import com.sion.concertbooking.domain.entity.ConcertSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertScheduleJpaRepository extends JpaRepository<ConcertSchedule, Long> {
}
