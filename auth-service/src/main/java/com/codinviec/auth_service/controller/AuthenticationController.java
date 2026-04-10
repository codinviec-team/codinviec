package com.codinviec.auth_service.controller;

import com.codinviec.auth_service.dto.TokenDTO;
import com.codinviec.auth_service.exception.security.ExpireTokenExceptionHandler;
import com.codinviec.auth_service.exception.security.RefreshTokenExceptionHandler;
import com.codinviec.auth_service.request.*;
import com.codinviec.auth_service.response.BaseResponse;
import com.codinviec.auth_service.service.AuthService;
import com.codinviec.auth_service.util.CookieHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    @Value("${client.url}")
    private String clientUrl;

    private final AuthService authService;
    private final CookieHelper cookieHelper;


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        TokenDTO tokenDTO = authService.login(request);
        cookieHelper.addRefreshTokenCookie(response, tokenDTO.getRefreshToken());
        cookieHelper.addAccessTokenCookies(response, tokenDTO.getAccessToken());
        return ResponseEntity.ok(BaseResponse.success(tokenDTO, "OK"));

    }

    @PostMapping("/register")
    public ResponseEntity<?> regsiter(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(BaseResponse.success(authService.register(registerRequest), "OK"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest,
                                          HttpServletResponse response) {
        if (refreshTokenRequest.getRefreshToken() == null || refreshTokenRequest.getRefreshToken().isEmpty()) {
            throw new RefreshTokenExceptionHandler("Không tìm thấy Refresh Token!");
        }
        try {
            TokenDTO tokenDTO = authService.refreshToken(refreshTokenRequest, response);
            cookieHelper.addRefreshTokenCookie(response, tokenDTO.getRefreshToken());
            cookieHelper.addAccessTokenCookies(response, tokenDTO.getAccessToken());
            return ResponseEntity.ok(BaseResponse.success(tokenDTO, "OK"));
        } catch (RefreshTokenExceptionHandler e) {
            cookieHelper.clearRefreshTokenCookie(response);
            cookieHelper.clearAccessTokenCookie(response);
            throw e;
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutRequest logoutRequest, HttpServletResponse response) {
        try {
            authService.logout(logoutRequest, response);
            return ResponseEntity.ok(BaseResponse.success("Đăng xuất thành công!", "OK"));
        } catch (RefreshTokenExceptionHandler | ExpireTokenExceptionHandler e) {
            throw new RefreshTokenExceptionHandler("Token không hợp lệ!");
        }
    }

    @GetMapping("/google")
    public void loginGoogle(HttpServletResponse response) throws IOException {
        String url = authService.buildUrlLoginGoogle();
        if (url == null) {
            throw new RuntimeException("Lỗi đăng nhập google vui lòng thử lại!");
        }
        response.sendRedirect(url);
    }


    @GetMapping("/google/callback")
    public void googleCallback(
            @RequestParam("code") String code,
            HttpServletResponse response, HttpServletRequest  request
            ) throws IOException {
        TokenDTO tokenDTO = authService.loginGoogleHandler(code);
        cookieHelper.addRefreshTokenCookie(response, tokenDTO.getRefreshToken());
        cookieHelper.addAccessTokenCookies(response, tokenDTO.getAccessToken());

        String redirectUrl = UriComponentsBuilder
                .fromUriString(clientUrl+"/login/google")
                .queryParam("token", tokenDTO.getAccessToken())
                .queryParam("refresh", tokenDTO.getRefreshToken())
                .build()
                .toUriString();
        response.sendRedirect(redirectUrl);
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestBody ResendOtpRequest resendOtpRequest) {
        authService.resendOtp(resendOtpRequest);
        return ResponseEntity.ok(BaseResponse.success("Resend otp user successfully!", "OK"));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyUserRequest verifyUserRequest) {
        authService.verifyUserOtp(verifyUserRequest);
        return ResponseEntity.ok(BaseResponse.success("Verify otp user successfully!", "OK"));
    }

}
