package com.codinviec.auth_service.exception.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginFaildExceptionHandler extends RuntimeException {
    private String message;
}