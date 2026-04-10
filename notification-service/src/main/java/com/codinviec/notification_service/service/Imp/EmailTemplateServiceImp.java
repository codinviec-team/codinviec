package com.codinviec.notification_service.service.Imp;

import com.codinviec.notification_service.service.EmailTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailTemplateServiceImp implements EmailTemplateService {
    private final TemplateEngine templateEngine;

    @Override
    public String buildRegisterTemplateEmail(String fullname) {
        Context context = new Context();
        context.setVariable("name", fullname);
        return templateEngine.process(
                "email/register-email-template",
                context
        );
    }

    @Override
    public String buildOtpTemplateEmail(String otp) {
        Context context = new Context();
        context.setVariable("otp", otp);
        return templateEngine.process(
                "email/otp-email-template",
                context
        );
    }
}
