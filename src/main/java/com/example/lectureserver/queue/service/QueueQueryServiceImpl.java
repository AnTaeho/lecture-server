package com.example.lectureserver.queue.service;

import static com.example.lectureserver.common.QueueConst.FIVE;
import static com.example.lectureserver.common.QueueConst.MINUTE;
import static com.example.lectureserver.common.QueueConst.PROCESSING_QUEUE_SIZE;

import com.example.lectureserver.queue.controller.dto.QueueResponse;
import com.example.lectureserver.queue.repository.QueueRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueQueryServiceImpl implements QueueQueryService {
    public final QueueRedisRepository queueRedisRepository;

    /*
    유저의 현재 큐 상태 확인
     */
    public QueueResponse findQueueStatus(Long userId, String token) {
        if (queueRedisRepository.isInProcessingQueue(userId)) {
            return QueueResponse.processing();
        }

        Integer position = queueRedisRepository.getWaitingQueuePosition(userId, token);
        if (position != null) {
            return QueueResponse.waiting(position, calculateEstimatedWaitSeconds(position));
        } else {
            return QueueResponse.notInQueue();
        }
    }

    private Integer calculateEstimatedWaitSeconds(int position) {
        int batchSize = PROCESSING_QUEUE_SIZE;
        int batchInterval = FIVE * MINUTE;
        int batches = position / batchSize;
        return batches * batchInterval;
    }
}
