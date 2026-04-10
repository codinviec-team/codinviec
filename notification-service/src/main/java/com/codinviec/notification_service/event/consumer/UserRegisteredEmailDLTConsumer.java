package com.codinviec.notification_service.event.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserRegisteredEmailDLTConsumer {

    @KafkaListener(topics = "notification-service.user-registered-email.DLT",
            groupId = "notification-dlt")
    public void handleDLT(String message) {
        log.error("DLT MESSAGE user-registered-topic: {}", message);

        // thực tế:
        // - lưu DB
        // - gửi Slack / Email alert
        // - chờ replay
    }
}