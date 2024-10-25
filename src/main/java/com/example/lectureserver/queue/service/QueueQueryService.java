package com.example.lectureserver.queue.service;

import com.example.lectureserver.queue.controller.dto.QueueResponse;

public interface QueueQueryService {
    QueueResponse findQueueStatus(Long userId, String token);

}
