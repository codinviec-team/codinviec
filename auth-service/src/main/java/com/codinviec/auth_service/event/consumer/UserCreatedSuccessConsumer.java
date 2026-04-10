package com.codinviec.auth_service.event.consumer;

import com.codinviec.auth_service.entity.UserEntity;
import com.codinviec.auth_service.event.payload.CreateUserCorePayload;
import com.codinviec.auth_service.event.payload.UserRegisterEmailPayload;
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
public class UserCreatedSuccessConsumer {
    private final UserServices userServices;
    private final AuthEventPublisher authEventPublisher;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "user-registered-success-topic", groupId = "auth-service.user-registered.activate")
    @Transactional
    public void activeUserRegistered(String message,
                                     Acknowledgment ack) {
        CreateUserCorePayload createUserCorePayload = objectMapper.readValue(message, CreateUserCorePayload.class);

//      Gửi sự kiện cho notificationservices gửi email thông báo thành
        authEventPublisher.publishUserRegisteredEmail(UserRegisterEmailPayload.builder()
                .email(createUserCorePayload.getEmail())
                .lastName(createUserCorePayload.getLastName() != null ? createUserCorePayload.getLastName() : "")
                .firstName(createUserCorePayload.getFirstName() != null ? createUserCorePayload.getFirstName() : "")
                .build());

//      set statas user entity của AuthServices
        UserEntity user = userServices.getUserByEmail(createUserCorePayload.getEmail());
        user.setStatus("ACTIVE");
        userServices.saveUser(user);
        ack.acknowledge();
    }
}
