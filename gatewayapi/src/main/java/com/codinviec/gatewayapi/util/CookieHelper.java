package com.codinviec.gatewayapi.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieHelper {
    @Value("${jwt.expiration.access}")
    private long expirationTime;

    @Value("${jwt.expiration.refresh}")
    private long expirationRefresh;

    public void addRefreshTokenCookie(ServerHttpResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None")
                .maxAge(expirationRefresh)
                .build();
        response.addCookie(cookie); // Đổi addHeader → addCookie
    }

    public void addAccessTokenCookies(ServerHttpResponse response, String accessToken) {
        ResponseCookie cookie = ResponseCookie.from("access_token", accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None")
                .maxAge(expirationTime)
                .build();
        response.addCookie(cookie); // Đổi addHeader → addCookie
    }

    public void clearRefreshTokenCookie(ServerHttpResponse response) {
        ResponseCookie cookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        response.addCookie(cookie);
    }

    public void clearAccessTokenCookie(ServerHttpResponse response) {
        ResponseCookie cookie = ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        response.addCookie(cookie);
    }
}
