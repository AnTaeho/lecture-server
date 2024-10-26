package com.example.lectureserver.reservation.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    private String email;
    private Long lectureId;
    private int seatNumber;

    public Reservation(String email, Long lectureId, int seatNumber) {
        this.email = email;
        this.lectureId = lectureId;
        this.seatNumber = seatNumber;
    }
}
