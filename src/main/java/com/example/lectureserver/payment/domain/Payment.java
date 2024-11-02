package com.example.lectureserver.payment.domain;

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
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    private int totalPrice;
    private Long userId;
    private Long reservationId;
    private int seatNumber;

    public Payment(int totalPrice, Long userId, Long reservationId, int seatNumber) {
        this.totalPrice = totalPrice;
        this.userId = userId;
        this.reservationId = reservationId;
        this.seatNumber = seatNumber;
    }
}