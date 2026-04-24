package com.codinviec.auth_service.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CookieHelper {
    @Value("${jwt.expiration.access}")
    private long expirationTime;

    @Value("${jwt.expiration.refresh}")
    private long expirationRefresh;


    public Optional<String> getAccessToken(HttpServletRequest request) {
        if (request.getCookies() == null) return Optional.empty();
        return Arrays.stream(request.getCookies())
                .filter(c -> "access_token".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    public Optional<String> getRefreshToken(HttpServletRequest request) {
        if (request.getCookies() == null) return Optional.empty();
        return Arrays.stream(request.getCookies())
                .filter(c -> "refresh_token".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true) // JavaScript không thể truy cập - BẢO MẬT
                .secure(true) // Chỉ dùng HTTPS trong production
                .path("/")
                .sameSite("None")
                .maxAge(expirationRefresh)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }


    public void addAccessTokenCookies(HttpServletResponse response, String accessToken) {
        ResponseCookie cookie = ResponseCookie.from("access_token", accessToken)
                .httpOnly(true) // JavaScript không thể truy cập - BẢO MẬT
                .secure(true) // Chỉ dùng HTTPS trong production
                .path("/")
                .sameSite("None")
                .maxAge(expirationTime)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }


    public void clearRefreshTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
    public void clearAccessTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
