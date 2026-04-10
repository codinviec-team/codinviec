package com.project.codinviec.exception.auth;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AlreadyLoggedInExceptionHandler extends RuntimeException {
    private String message;
}