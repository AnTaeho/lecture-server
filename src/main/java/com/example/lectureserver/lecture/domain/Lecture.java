package com.example.lectureserver.lecture.domain;

import com.example.lectureserver.seat.domain.Seat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_id")
    private Long id;

    private String title;
    private String description;
    private String lecturer;

    private int size;
    private int price;

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.REMOVE)
    private final List<Seat> seats = new ArrayList<>();

    public Lecture(String title, String description, String lecturer, int size, int price) {
        this.title = title;
        this.description = description;
        this.lecturer = lecturer;
        this.size = size;
        this.price = price;
    }

    public void addSeat(Seat seat) {
        this.seats.add(seat);
    }
}
