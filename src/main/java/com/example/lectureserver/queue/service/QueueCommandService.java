package com.example.lectureserver.queue.service;

public interface QueueCommandService {
    String issueTokenAndEnqueue(Long userId);

    void updateWaitingToProcessing();

    void removeExpiredToken();

    void removeTokenInWaitingQueue(Long userId, String token);

    void removeTokenInProcessingQueue(Long userId, String token);
}
