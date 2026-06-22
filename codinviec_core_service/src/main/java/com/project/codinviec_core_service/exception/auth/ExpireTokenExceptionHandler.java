package com.project.codinviec_core_service.exception.auth;

public class ExpireTokenExceptionHandler extends RuntimeException {
    public ExpireTokenExceptionHandler(String message) {
        super(message);
    }
}
