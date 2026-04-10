package com.codinviec.gatewayapi.exception;


import com.codinviec.gatewayapi.exception.security.AccessTokenExceptionHandler;
import com.codinviec.gatewayapi.exception.security.RefreshTokenExceptionHandler;
import com.codinviec.gatewayapi.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    // Báo lỗi chung 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> handleGeneralException(
            Exception ex, ServerWebExchange exchange) {  // Đổi HttpServletRequest → ServerWebExchange

        ServerHttpRequest request = exchange.getRequest();

        log.error("Unexpected Exception: {} | URI: {} | Method: {} | Message: {}",
                ex.getClass().getName(),
                request.getURI(),           // getRequestURI() → getURI()
                request.getMethod(),        // getMethod() giữ nguyên
                ex.getMessage(),
                ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BaseResponse.error("Đã xảy ra lỗi hệ thống. Vui lòng thử lại sau!",
                        HttpStatus.INTERNAL_SERVER_ERROR));
    }



    // LỖI VALIDATION
    // Hứng lỗi validation khi dùng @Valid @RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(
            MethodArgumentNotValidException ex, ServerWebExchange exchange) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((e) -> errors.put(e.getField(), e.getDefaultMessage()));

        ServerHttpRequest request = exchange.getRequest();

        // Log đầy đủ chi tiết cho developer
        log.warn("Validation Error: {} | URI: {} | Method: {} | Errors: {}",
                ex.getClass().getSimpleName(),
                request.getURI(),
                request.getMethod(),
                errors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.validationError(errors));
    }

    // AccessToken handle exception
    @ExceptionHandler(AccessTokenExceptionHandler.class)
    public ResponseEntity<BaseResponse> handleAccessTokenException(
            AccessTokenExceptionHandler ex, ServerWebExchange exchange) {

        ServerHttpRequest request = exchange.getRequest();


        log.error("AccessTokenExceptionHandler: {} | URI: {} | Method: {} | Message: {}",
                ex.getClass().getSimpleName(),
                request.getURI(),
                request.getMethod(),
                ex.getMessage(),
                ex);

        String userMessage = ex.getMessage() != null && !ex.getMessage().isBlank()
                ? ex.getMessage()
                : "Token không hợp lệ. Vui lòng đăng nhập lại!";
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(BaseResponse.error(userMessage, HttpStatus.UNAUTHORIZED));
    }



    // RefreshToken handle exception
    @ExceptionHandler(RefreshTokenExceptionHandler.class)
    public ResponseEntity<BaseResponse> handleRefreshTokenException(
            RefreshTokenExceptionHandler ex,  ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        log.error("RefreshTokenExceptionHanlder: {} | URI: {} | Method: {} | Message: {}",
                ex.getClass().getSimpleName(),
                request.getURI(),
                request.getMethod(),
                ex.getMessage(),
                ex);

        String userMessage = ex.getMessage() != null && !ex.getMessage().isBlank()
                ? ex.getMessage()
                : "Token không hợp lệ. Vui lòng đăng nhập lại!";
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(BaseResponse.error(userMessage, HttpStatus.UNAUTHORIZED));
    }
}
