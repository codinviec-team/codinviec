package com.codinviec.gatewayapi.exception.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenExceptionHandler extends RuntimeException {
    private String message;
}
