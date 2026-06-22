package com.project.codinviec_core_service.exception.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailFailExceptionHandler extends RuntimeException {
    private String message;
}
