package com.project.codinviec.event.publish;


import com.project.codinviec.entity.auth.OutboxEventEntity;
import com.project.codinviec.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@RequiredArgsConstructor
@Slf4j

public class OutboxPublisher {

    private final AtomicBoolean hasPendingEvents = new AtomicBoolean(false);
    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    // UserService gọi cái này sau khi lưu outbox
    public void markHasPendingEvents() {
        hasPendingEvents.set(true);
    }

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void publishPendingEvents() {
        // Không có event mới thì bỏ qua, không query DB
        if (!hasPendingEvents.compareAndSet(true, false)) {
            return;
        }

        List<OutboxEventEntity> pending = outboxRepository
                .findPendingAndFailedEvents(LocalDateTime.now());

        pending.forEach(event -> {
            try {
                // Gửi lên Kafka topic tương ứng với eventType
                kafkaTemplate.send(event.getEventType(), event.getPayload())
                        .whenComplete((result, ex) -> {
                            if (ex == null) {
                                // Gửi thành công → đánh dấu PUBLISHED
                                event.setStatus("PUBLISHED");
                                event.setPublishedAt(LocalDateTime.now());
                            } else {
                                // Gửi thất bại → đánh dấu FAILED để retry sau
                                event.setStatus("FAILED");
                            }
                            outboxRepository.save(event);
                        });
            } catch (Exception e) {

                event.setRetryCount(event.getRetryCount() + 1);
                if (event.getRetryCount() >= 5) {
                    // Thử 5 lần vẫn lỗi → bỏ cuộc, cần xem xét thủ công
                    event.setStatus("DEAD");
                    log.error("Event {} đã thất bại {} lần, chuyển sang DEAD",
                            event.getId(), event.getRetryCount());
                } else {
                    // Còn lần retry → tính thời gian retry tiếp theo
                    event.setStatus("FAILED");
                    event.setNextRetryAt(calculateNextRetry(event.getRetryCount()));
                }                outboxRepository.save(event);
            }
        });
    }

    private LocalDateTime calculateNextRetry(int retryCount) {
        // Exponential backoff — càng retry nhiều thì chờ càng lâu
        // lần 1: 10s, lần 2: 30s, lần 3: 90s, lần 4: 270s...
        long seconds = (long) Math.pow(3, retryCount) * 10;
        return LocalDateTime.now().plusSeconds(seconds);
    }
}

