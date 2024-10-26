package com.example.lectureserver.seat.repository;

import com.example.lectureserver.seat.domain.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
