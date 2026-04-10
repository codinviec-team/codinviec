package com.codinviec.notification_service.service;

public interface EmailService {
    void sendEmailRegister(String to, String subject, String html);
}
