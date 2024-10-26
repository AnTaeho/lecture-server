package com.example.lectureserver.lecture.controller.dto;

public record LectureSimpleResponse(
        String title,
        String lecturer,
        int size
) {
}
