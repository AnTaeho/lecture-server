package com.example.lectureserver.lecture.repository;

import com.example.lectureserver.lecture.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
}
