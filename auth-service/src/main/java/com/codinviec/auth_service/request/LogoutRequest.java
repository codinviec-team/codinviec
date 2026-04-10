package com.codinviec.auth_service.request;

import lombok.Data;

@Data
public class LogoutRequest {
    String refreshToken;
}
