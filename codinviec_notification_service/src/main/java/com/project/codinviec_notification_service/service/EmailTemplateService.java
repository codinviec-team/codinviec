package com.project.codinviec_notification_service.service;

public interface EmailTemplateService {
    String buildRegisterTemplateEmail(String name);
    String buildOtpTemplateEmail(String otp);
}
