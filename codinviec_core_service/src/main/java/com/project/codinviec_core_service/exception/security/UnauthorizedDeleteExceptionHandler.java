package com.project.codinviec_core_service.exception.security;

public class UnauthorizedDeleteExceptionHandler extends RuntimeException {
    public UnauthorizedDeleteExceptionHandler(String message) {
        super(message);
    }
}
