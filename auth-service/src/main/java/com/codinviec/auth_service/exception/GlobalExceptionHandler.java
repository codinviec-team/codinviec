package com.codinviec.auth_service.exception;

import com.codinviec.auth_service.exception.common.NotFoundIdExceptionHandler;
import com.codinviec.auth_service.exception.event.*;
import com.codinviec.auth_service.exception.security.*;
import com.codinviec.auth_service.response.BaseResponse;
import com.codinviec.auth_service.util.CookieHelper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final CookieHelper cookieHelper;

    // LỖI VALIDATION
    // Hứng lỗi validation khi dùng @Valid @RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((e) -> errors.put(e.getField(), e.getDefaultMessage()));

        // Log đầy đủ chi tiết cho developer
        log.warn("Validation Error: {} | URI: {} | Method: {} | Errors: {}",
                ex.getClass().getSimpleName(),
                request.getRequestURI(),
                request.getMethod(),
                errors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.validationError(errors));
    }


    // Báo lỗi chung 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> handleGeneralException(
            Exception ex, HttpServletRequest request) {
        // Log đầy đủ chi tiết cho developer
        log.error("Unexpected Exception: {} | URI: {} | Method: {} | Message: {}",
                ex.getClass().getName(),
                request.getRequestURI(),
                request.getMethod(),
                ex.getMessage(),
                ex);

        // Trả về message generic cho FE (không tiết lộ chi tiết lỗi)
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BaseResponse.error("Đã xảy ra lỗi hệ thống. Vui lòng thử lại sau!",
                        HttpStatus.INTERNAL_SERVER_ERROR));
    }


    // AccessToken handle exception
    @ExceptionHandler(AccessTokenExceptionHandler.class)
    public ResponseEntity<BaseResponse> handleAccessTokenException(
            AccessTokenExceptionHandler ex, HttpServletRequest request) {
        log.error("AccessTokenExceptionHandler: {} | URI: {} | Method: {} | Message: {}",
                ex.getClass().getSimpleName(),
                request.getRequestURI(),
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
            RefreshTokenExceptionHandler ex, HttpServletRequest request) {
        log.error("RefreshTokenExceptionHanlder: {} | URI: {} | Method: {} | Message: {}",
                ex.getClass().getSimpleName(),
                request.getRequestURI(),
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

    // Block user
    @ExceptionHandler(BlockLoginUserExceptionHandler.class)
    public ResponseEntity<BaseResponse> handleBlockUserException(
            BlockLoginUserExceptionHandler ex, HttpServletRequest request) {
        log.error("BlockLoginUserExceptionHandler: {} | URI: {} | Method: {} | Message: {}",
                ex.getClass().getSimpleName(),
                request.getRequestURI(),
                request.getMethod(),
                ex.getMessage(),
                ex);

        String userMessage = ex.getMessage() != null && !ex.getMessage().isBlank()
                ? "Bạn đã bị khóa đăng nhập tạm thời! " + ex.getMessage()
                : "Bạn đã bị khóa đăng nhập tạm thời. Vui lòng thử lại sau!";
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(BaseResponse.error(userMessage, HttpStatus.FORBIDDEN));
    }

    // LoginFail handle exception
    @ExceptionHandler(LoginFaildExceptionHandler.class)
    public ResponseEntity<BaseResponse> handleLoginFailException(
            LoginFaildExceptionHandler ex, HttpServletRequest request) {
        log.error("LoginFailExceptionHandler: {} | URI: {} | Method: {} | Message: {}",
                ex.getClass().getSimpleName(),
                request.getRequestURI(),
                request.getMethod(),
                ex.getMessage(),
                ex);

        String userMessage = ex.getMessage() != null && !ex.getMessage().isBlank()
                ? ex.getMessage()
                : "Đăng nhập thất bại!";
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.error(userMessage, HttpStatus.BAD_REQUEST));
    }

    // Wrong password
    @ExceptionHandler(WrongPasswordOrEmailExceptionHandler.class)
    public ResponseEntity<BaseResponse> handleWrongPasswordException(
            WrongPasswordOrEmailExceptionHandler ex, HttpServletRequest request) {
        log.error("WrongPasswordExceptionHandler: {} | URI: {} | Method: {} | Message: {}",
                ex.getClass().getSimpleName(),
                request.getRequestURI(),
                request.getMethod(),
                ex.getMessage(),
                ex);

        String userMessage = "Tài khoản hoặc mật khẩu không chính xác!";
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(BaseResponse.error(userMessage, HttpStatus.UNAUTHORIZED));
    }
    @ExceptionHandler(AlreadyLoggedInExceptionHandler.class)
    public ResponseEntity<BaseResponse> handleAlreadyLoggedInException(
            AlreadyLoggedInExceptionHandler ex, HttpServletRequest request) {
        log.error("AlreadyLoggedInException: {} | URI: {} | Method: {} | Message: {}",
                ex.getClass().getSimpleName(),
                request.getRequestURI(),
                request.getMethod(),
                ex.getMessage(),
                ex);

        String userMessage = ex.getMessage() != null && !ex.getMessage().isBlank()
                ? ex.getMessage()
                : "Tài khoản này đã được đăng nhập từ thiết bị khác!";
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(BaseResponse.error(userMessage, HttpStatus.CONFLICT));
    }

    // Lỗi emai trùng khi thêm
    @ExceptionHandler(EmailAlreadyExistsExceptionHandler.class)
    public ResponseEntity<BaseResponse> handleEmailAlreadyExisException(
            EmailAlreadyExistsExceptionHandler ex, HttpServletRequest request) {
        log.error("EmailAlreadyExists: {} | URI: {} | Method: {} | Message: {}",
                ex.getClass().getSimpleName(),
                request.getRequestURI(),
                request.getMethod(),
                ex.getMessage(),
                ex);

        String userMessage = ex.getMessage() != null && !ex.getMessage().isBlank()
                ? ex.getMessage()
                : "Email này đã được sử dụng. Vui lòng chọn email khác!";
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(BaseResponse.error(userMessage, HttpStatus.CONFLICT));
    }

    @ExceptionHandler(NotFoundIdExceptionHandler.class)
    public ResponseEntity<BaseResponse> handleNotFoundIdExceptionHandler(
            NotFoundIdExceptionHandler ex, HttpServletRequest request) {
        // Log đầy đủ chi tiết cho developer
        log.error("NotFoundIdExceptionHandler: {} | URI: {} | Method: {} | Message: {}",
                ex.getClass().getSimpleName(),
                request.getRequestURI(),
                request.getMethod(),
                ex.getMessage(),
                ex);

        // Trả về message user-friendly cho FE
        String userMessage = ex.getMessage() != null && !ex.getMessage().isBlank()
                ? ex.getMessage()
                : "Không tìm thấy dữ liệu yêu cầu";
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse.error(userMessage, HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(ExpireTokenExceptionHandler.class)
    public ResponseEntity<?> handleRefreshTokenExpired(
            ExpireTokenExceptionHandler ex, HttpServletRequest request, HttpServletResponse response) {
        cookieHelper.clearRefreshTokenCookie(response);

        log.error("ExpireTokenExceptionHanlder: {} | URI: {} | Method: {} | Message: {}",
                ex.getClass().getSimpleName(),
                request.getRequestURI(),
                request.getMethod(),
                ex.getMessage(),
                ex);

        String userMessage = ex.getMessage() != null && !ex.getMessage().isBlank()
                ? ex.getMessage()
                : "Phiên đăng nhập của bạn đã hết hạn. Vui lòng đăng nhập lại!";
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(BaseResponse.error(userMessage, HttpStatus.UNAUTHORIZED));
    }
//  Lối JWT
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<?> handleJwtException(
            JwtException ex, HttpServletRequest request, HttpServletResponse response) {
        cookieHelper.clearRefreshTokenCookie(response);

        log.error("JwtException: {} | URI: {} | Method: {} | Message: {}",
                ex.getClass().getSimpleName(),
                request.getRequestURI(),
                request.getMethod(),
                ex.getMessage(),
                ex);

        String userMessage = ex.getMessage() != null && !ex.getMessage().isBlank()
                ? ex.getMessage()
                : "Đăng nhập hết hạn!";
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(BaseResponse.error(userMessage, HttpStatus.UNAUTHORIZED));
    }

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
                .body(BaseResponse.error(userMessage, HttpStatus.BAD_REQUEST));
    }

    // Lỗi gửi email khi verify
    @ExceptionHandler(SendEmailVerifyFail.class)
    public ResponseEntity<BaseResponse> handleSendEmailVerifyFailException(
            SendEmailVerifyFail ex, HttpServletRequest request) {
        log.error("SendEmailVerifyFail: {} | URI: {} | Method: {} | Message: {}",
                ex.getClass().getSimpleName(),
                request.getRequestURI(),
                request.getMethod(),
                ex.getMessage(),
                ex);

        String userMessage = ex.getMessage() != null && !ex.getMessage().isBlank()
                ? ex.getMessage()
                : "Gửi email verify thất bại!";
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(BaseResponse.error(userMessage, HttpStatus.BAD_REQUEST));
    }

    // Lỗi user không được gửi đi đến core services
    @ExceptionHandler(UserRegisteredFail.class)
    public ResponseEntity<BaseResponse> handleUserRegisteredFailException(
            UserRegisteredFail ex, HttpServletRequest request) {
        log.error("UserRegisteredFail: {} | URI: {} | Method: {} | Message: {}",
                ex.getClass().getSimpleName(),
                request.getRequestURI(),
                request.getMethod(),
                ex.getMessage(),
                ex);

        String userMessage = ex.getMessage() != null && !ex.getMessage().isBlank()
                ? ex.getMessage()
                : "Gửi user thất bại!";
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(BaseResponse.error(userMessage, HttpStatus.BAD_REQUEST));
    }

    // Lỗi verify user
    @ExceptionHandler(SendVerifyOtpUserFail.class)
    public ResponseEntity<BaseResponse> handleVerifyUserFailException(
            SendVerifyOtpUserFail ex, HttpServletRequest request) {
        log.error("SendVerifyOtpUserFail: {} | URI: {} | Method: {} | Message: {}",
                ex.getClass().getSimpleName(),
                request.getRequestURI(),
                request.getMethod(),
                ex.getMessage(),
                ex);

        String userMessage = ex.getMessage() != null && !ex.getMessage().isBlank()
                ? ex.getMessage()
                : "Gửi otp thất bại!";
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(BaseResponse.error(userMessage, HttpStatus.BAD_REQUEST));
    }

    // Lỗi resend verify user
    @ExceptionHandler(ResendVerifyOtpUserFail.class)
    public ResponseEntity<BaseResponse> handleResendVerifyOtpUserFailException(
            ResendVerifyOtpUserFail ex, HttpServletRequest request) {
        log.error("ResendVerifyOtpUserFail: {} | URI: {} | Method: {} | Message: {}",
                ex.getClass().getSimpleName(),
                request.getRequestURI(),
                request.getMethod(),
                ex.getMessage(),
                ex);

        String userMessage = ex.getMessage() != null && !ex.getMessage().isBlank()
                ? ex.getMessage()
                : "Gửi lại otp cho user thất bại!";
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(BaseResponse.error(userMessage, HttpStatus.BAD_REQUEST));
    }

    // Lỗi resend quá số lần
    @ExceptionHandler(ResendVerifyOtpOverCounter.class)
    public ResponseEntity<BaseResponse> handleResendVerifyOtpOverCounterException(
            ResendVerifyOtpOverCounter ex, HttpServletRequest request) {
        log.error("ResendVerifyOtpOverCounter: {} | URI: {} | Method: {} | Message: {}",
                ex.getClass().getSimpleName(),
                request.getRequestURI(),
                request.getMethod(),
                ex.getMessage(),
                ex);

        String userMessage = ex.getMessage() != null && !ex.getMessage().isBlank()
                ? ex.getMessage()
                : "Số lần xác thực quá nhiều vui lòng truy cập lại sau!";
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(BaseResponse.error(userMessage, HttpStatus.BAD_REQUEST));
    }


    // Lỗi người dùng chưa đăng ký
    @ExceptionHandler(UnregisteredUsers.class)
    public ResponseEntity<BaseResponse> handleUnregisteredUsersException(
            UnregisteredUsers ex, HttpServletRequest request) {
        log.error("UnregisteredUsers: {} | URI: {} | Method: {} | Message: {}",
                ex.getClass().getSimpleName(),
                request.getRequestURI(),
                request.getMethod(),
                ex.getMessage(),
                ex);

        String userMessage = ex.getMessage() != null && !ex.getMessage().isBlank()
                ? ex.getMessage()
                : "Người dùng chưa đăng ký!";
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(BaseResponse.error(userMessage, HttpStatus.BAD_REQUEST));
    }

    // Lỗi người dùng chưa đăng ký
    @ExceptionHandler(VerifyOtpFail.class)
    public ResponseEntity<BaseResponse> handleVerifyOtpFailException(
            VerifyOtpFail ex, HttpServletRequest request) {
        log.error("VerifyOtpFail: {} | URI: {} | Method: {} | Message: {}",
                ex.getClass().getSimpleName(),
                request.getRequestURI(),
                request.getMethod(),
                ex.getMessage(),
                ex);

        String userMessage = ex.getMessage() != null && !ex.getMessage().isBlank()
                ? ex.getMessage()
                : "Sai mã otp!";
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(BaseResponse.error(userMessage, HttpStatus.BAD_REQUEST));
    }

}
