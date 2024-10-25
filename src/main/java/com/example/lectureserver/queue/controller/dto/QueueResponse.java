package com.example.lectureserver.queue.controller.dto;

import static com.example.lectureserver.common.QueueConst.*;

public record QueueResponse(
        String status,
        Integer waitingQueueCount,
        Integer estimatedWaitTime
) {
    public static QueueResponse processing() {
            return new QueueResponse(PROCESSING, ZERO, ZERO);
    }

    public static QueueResponse waiting(Integer position, Integer estimatedWaitTime) {
        return new QueueResponse(WAITING, position, estimatedWaitTime);
    }

    public static QueueResponse notInQueue() {
        return new QueueResponse(NOT_IN_QUEUE, ZERO, ZERO);
    }
}
