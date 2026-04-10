package com.codinviec.auth_service.exception.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendVerifyOtpUserFail extends RuntimeException {
    private String message;
}
