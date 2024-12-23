package com.example.lectureserver.seat.domain;

import com.example.lectureserver.lecture.domain.Lecture;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long id;

    private int seatNumber;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    public Seat(int seatNumber, Lecture lecture) {
        this.seatNumber = seatNumber;
        this.lecture = lecture;
        this.status = SeatStatus.AVAILABLE;
        lecture.addSeat(this);
    }

    public void updateStatus() {
        this.status = SeatStatus.UNAVAILABLE;
    }
}
