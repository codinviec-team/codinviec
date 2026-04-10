package com.codinviec.auth_service.exception.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BlockLoginUserExceptionHandler extends RuntimeException {
    private String message;
}