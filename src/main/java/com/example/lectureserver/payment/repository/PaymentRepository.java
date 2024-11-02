package com.example.lectureserver.payment.repository;

import com.example.lectureserver.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
