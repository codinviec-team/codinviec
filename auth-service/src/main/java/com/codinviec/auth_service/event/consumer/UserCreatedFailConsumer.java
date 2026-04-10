package com.codinviec.auth_service.event.consumer;


import com.codinviec.auth_service.event.payload.CreateUserCorePayload;
import com.codinviec.auth_service.event.publish.AuthEventPublisher;
import com.codinviec.auth_service.service.UserServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserCreatedFailConsumer {
    private final UserServices userServices;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "user-registered-fail-topic", groupId = "auth-service.user-registered.rollback")
    @Transactional
    public void rollbackUserRegistered(String message,
                                       Acknowledgment ack) {
        CreateUserCorePayload createUserCorePayload = objectMapper.readValue(message, CreateUserCorePayload.class);
        log.warn("Auth services tạo thất bại cho user {}, tiến hành rollback", createUserCorePayload.getId());
        userServices.deleteUser(createUserCorePayload.getEmail());
        ack.acknowledge();
    }

}
