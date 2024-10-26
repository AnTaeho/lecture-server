package com.example.lectureserver.lecture.controller.dto;

public record LectureRegisterRequest(
        String title,
        String description,
        String lecturer,
        int size
) {
}
