package com.project.codinviec_notification_service.service;

public interface EmailService {
    void sendEmailRegister(String to, String subject, String html);
}
