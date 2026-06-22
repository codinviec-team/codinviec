package com.project.codinviec_core_service.exception.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BlockLoginUserExceptionHandler extends RuntimeException {
    private String message;
}
