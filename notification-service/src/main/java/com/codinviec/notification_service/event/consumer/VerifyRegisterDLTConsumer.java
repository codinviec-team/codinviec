package com.codinviec.notification_service.event.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class VerifyRegisterDLTConsumer {
    @KafkaListener(topics = "user-verify-topic.DLT",
            groupId = "notification-service.user-email.dlt")
    public void handleDLT(String message) {
        log.error("DLT MESSAGE user-verify-topic: {}", message);

        // thực tế:
        // - lưu DB
        // - gửi Slack / Email alert
        // - chờ replay
    }
}
