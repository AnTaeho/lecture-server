package com.example.lectureserver.reservation.repository;

import com.example.lectureserver.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
