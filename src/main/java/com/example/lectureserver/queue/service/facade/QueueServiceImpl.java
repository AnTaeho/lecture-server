package com.example.lectureserver.queue.service.facade;

import com.example.lectureserver.queue.controller.dto.QueueResponse;
import com.example.lectureserver.queue.controller.dto.TokenResponse;
import com.example.lectureserver.queue.service.QueueCommandService;
import com.example.lectureserver.queue.service.QueueQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueServiceImpl implements QueueService {
    private final QueueCommandService queueCommandService;
    private final QueueQueryService queueQueryService;

    @Override
    public TokenResponse issueTokenAndEnqueue(Long userId) {
        return new TokenResponse(queueCommandService.issueTokenAndEnqueue(userId));
    }

    @Override
    public void dequeueWaitingQueue(Long userId, String token) {
        queueCommandService.removeTokenInWaitingQueue(userId, token);
    }

    @Override
    public void dequeueProcessingQueue(Long userId, String token) {
        queueCommandService.removeTokenInProcessingQueue(userId, token);
    }

    @Override
    public QueueResponse findQueueStatus(Long userId, String token) {
        return queueQueryService.findQueueStatus(userId, token);
    }

}
