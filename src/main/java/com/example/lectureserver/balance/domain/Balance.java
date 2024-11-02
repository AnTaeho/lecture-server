package com.example.lectureserver.balance.domain;

import com.example.lectureserver.user.domain.User;
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
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "balance_id")
    private Long id;

    private int amount;

    private Long userId;

    public Balance(User user) {
        this.amount = 0;
        this.userId = user.getId();
    }

    public void charge(int amount) {
        this.amount += amount;
    }

    public void use(int amount) {
        if (this.amount < amount) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }
        this.amount -= amount;
    }
}
