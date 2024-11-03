package com.example.lectureserver.reservation.service;

import com.example.lectureserver.reservation.domain.Reservation;
import com.example.lectureserver.reservation.repository.ReservationRepository;
import com.example.lectureserver.seat.domain.Seat;
import com.example.lectureserver.seat.repository.SeatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final SeatRepository seatRepository;

    public void reserveSeat(String email, Long lectureId, List<Integer> seatNumbers) {
        List<Seat> seatWithPessimisticLock = seatRepository.findSeatWithPessimisticLock(lectureId, seatNumbers);
        for (Seat seat : seatWithPessimisticLock) {
            seat.updateStatus();

            Reservation reservation = new Reservation(email, lectureId, seat.getSeatNumber());
            reservationRepository.save(reservation);
        }
    }

}
