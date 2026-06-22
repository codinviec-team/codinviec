package com.project.codinviec_core_service.exception.common;

public class ConflictExceptionHandler extends RuntimeException {
    public ConflictExceptionHandler(String message) {
        super(message);
    }
}
