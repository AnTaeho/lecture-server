package com.example.lectureserver.lecture.controller.dto;

public record LectureDetailResponse(
        String title,
        String description,
        String lecturer,
        int size
) {
}
