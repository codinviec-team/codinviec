package com.codinviec.auth_service.service;

import com.codinviec.auth_service.dto.RegisterDTO;
import com.codinviec.auth_service.dto.TokenDTO;
import com.codinviec.auth_service.request.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthService {
    TokenDTO login(LoginRequest loginRequest);
    String register(RegisterRequest registerRequest);
    TokenDTO refreshToken(RefreshTokenRequest refreshTokenRequest, HttpServletResponse response);
    void logout(LogoutRequest logoutRequest, HttpServletResponse response);
    void resendOtp(ResendOtpRequest resendOtpRequest);
    void verifyUserOtp(VerifyUserRequest verifyUserRequest);
    TokenDTO loginGoogleHandler(String code) throws IOException;
    String buildUrlLoginGoogle();
}
