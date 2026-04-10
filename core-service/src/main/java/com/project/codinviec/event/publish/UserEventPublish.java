package com.project.codinviec.event.publish;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.codinviec.entity.auth.OutboxEventEntity;
import com.project.codinviec.event.payload.CreateUserCorePayload;
import com.project.codinviec.exception.auth.CreatedUserFail;
import com.project.codinviec.service.OutboxServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserEventPublish {
    private final ObjectMapper objectMapper;
    private final OutboxServices outboxServices;
    private final OutboxPublisher outboxPublisher;

    public void publishRegisterSuccess(CreateUserCorePayload createUserCorePayload) {
        try {
            String eventPayload = objectMapper.writeValueAsString(createUserCorePayload);
            outboxServices.addEventToOutBox(OutboxEventEntity.builder()
                    .eventType("user-registered-success-topic")
                    .payload(eventPayload)
                    .status("PENDING")
                    .createdDate(LocalDateTime.now())
                    .build());
            outboxPublisher.markHasPendingEvents();
        } catch (RuntimeException  e) {
            throw new CreatedUserFail();
        }
    }

    public void publishRegisterFail(CreateUserCorePayload createUserCorePayload) {
        try {
            String eventPayload = objectMapper.writeValueAsString(createUserCorePayload);
            outboxServices.addEventToOutBox(OutboxEventEntity.builder()
                    .eventType("user-registered-fail-topic")
                    .payload(eventPayload)
                    .status("PENDING")
                    .createdDate(LocalDateTime.now())
                    .build());
            outboxPublisher.markHasPendingEvents();
        } catch (RuntimeException  e) {
            throw new CreatedUserFail();
        }
    }
}
