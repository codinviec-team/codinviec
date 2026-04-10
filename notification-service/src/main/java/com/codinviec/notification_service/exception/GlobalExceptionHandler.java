package com.codinviec.notification_service.exception;

import com.codinviec.notification_service.exception.event.SendEmailRegisterFail;
import com.codinviec.notification_service.response.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    // Lỗi gửi email
    @ExceptionHandler(SendEmailRegisterFail.class)
    public ResponseEntity<BaseResponse> handleSendEmailRegisterFailException(
            SendEmailRegisterFail ex, HttpServletRequest request) {
        log.error("SendEmailRegisterFail: {} | URI: {} | Method: {} | Message: {}",
                ex.getClass().getSimpleName(),
                request.getRequestURI(),
                request.getMethod(),
                ex.getMessage(),
                ex);

        String userMessage = ex.getMessage() != null && !ex.getMessage().isBlank()
                ? ex.getMessage()
                : "Gửi email thất bại!";
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(BaseResponse.error(userMessage, HttpStatus.CONFLICT));
    }
}
