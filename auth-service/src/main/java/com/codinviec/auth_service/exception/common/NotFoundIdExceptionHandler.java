package com.codinviec.auth_service.exception.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotFoundIdExceptionHandler extends RuntimeException{
    private String message;
}
