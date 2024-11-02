package com.example.lectureserver.lecture.controller;

import com.example.lectureserver.lecture.controller.dto.LectureDetailResponse;
import com.example.lectureserver.lecture.controller.dto.LectureListResponse;
import com.example.lectureserver.lecture.controller.dto.LectureRegisterRequest;
import com.example.lectureserver.lecture.controller.dto.LectureResponse;
import com.example.lectureserver.lecture.controller.dto.ReservationRequest;
import com.example.lectureserver.lecture.service.LectureService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lectures")
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;

    @PostMapping
    public ResponseEntity<LectureResponse> registerLecture(@RequestBody LectureRegisterRequest lectureRegisterRequest) {
        return ResponseEntity.ok(lectureService.registerLecture(lectureRegisterRequest));
    }

    @PostMapping("/{lectureId}")
    public ResponseEntity<LectureResponse> reserveLecture(@PathVariable Long lectureId,
                                                              @RequestBody ReservationRequest request) {
        String email = UUID.randomUUID().toString();
        return ResponseEntity.ok(lectureService.reserveLecture(request, email, lectureId));
    }

    @GetMapping
    public ResponseEntity<LectureListResponse> getAllLecture() {
        return ResponseEntity.ok(lectureService.getALlLecture());
    }

    @GetMapping("/{lectureId}")
    public ResponseEntity<LectureDetailResponse> getLecture(@PathVariable Long lectureId) {
        return ResponseEntity.ok(lectureService.getLectureDetail(lectureId));
    }
}
