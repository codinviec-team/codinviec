package com.project.codinviec_notification_service.service.Imp;

import com.project.codinviec_notification_service.exception.event.SendEmailRegisterFail;
import com.project.codinviec_notification_service.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailServiceImp implements EmailService {


    private final JavaMailSenderImpl mailSender;

    @Override
    public void sendEmailRegister(String to, String subject, String html) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true); // true = HTML

            mailSender.send(message);
        } catch (Exception e) {
            throw new SendEmailRegisterFail();
        }
    }
}