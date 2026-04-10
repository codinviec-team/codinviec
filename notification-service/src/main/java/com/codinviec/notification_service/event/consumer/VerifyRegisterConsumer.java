package com.codinviec.notification_service.event.consumer;

import com.codinviec.notification_service.event.payload.VerifyRegisterPayload;
import com.codinviec.notification_service.exception.event.SendEmailRegisterFail;
import com.codinviec.notification_service.service.EmailTemplateService;
import com.codinviec.notification_service.service.Imp.EmailServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class VerifyRegisterConsumer {
    private final EmailServiceImp emailService;
    private final EmailTemplateService emailTemplateService;
    private final ObjectMapper objectMapper;


    @KafkaListener(
            topics = "user-verify-topic",
            groupId = "notification-service.user-email.verify"
    )
    public void handleUserRegistered(String message,
                                     Acknowledgment ack) {
        try {
            VerifyRegisterPayload verifyRegisterPayload = objectMapper.readValue(message, VerifyRegisterPayload.class);
            String html = emailTemplateService.buildOtpTemplateEmail(verifyRegisterPayload.getOtp());
            emailService.sendEmailRegister(
                    verifyRegisterPayload.getEmail(),
                    "Thông báo xác thực tài khoản!",
                    html);
//            commit đi kafka
            ack.acknowledge();
        } catch (RuntimeException e) {
            throw new SendEmailRegisterFail();
        }
    }
}
