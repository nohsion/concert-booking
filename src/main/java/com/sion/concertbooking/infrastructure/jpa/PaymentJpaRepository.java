package com.sion.concertbooking.infrastructure.jpa;

import com.sion.concertbooking.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {
}
