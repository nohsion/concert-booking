package com.sion.concertbooking.domain.repository;

import com.sion.concertbooking.domain.entity.ConcertSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertScheduleRepository extends JpaRepository<ConcertSchedule, Long> {
}
