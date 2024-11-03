package com.example.lectureserver.seat.repository;

import com.example.lectureserver.seat.domain.Seat;
import jakarta.persistence.LockModeType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("select s from Seat s where s.lecture.id = :lectureId and s.seatNumber in :seatNumbers and s.status = 'AVAILABLE'")
    List<Seat> findSeatWithPessimisticLock(@Param("lectureId") Long lectureId,
                                           @Param("seatNumbers") List<Integer> seatNumbers);

}
