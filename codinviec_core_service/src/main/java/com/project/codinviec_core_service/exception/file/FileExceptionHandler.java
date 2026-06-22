package com.project.codinviec_core_service.exception.file;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileExceptionHandler extends RuntimeException {
    private String message;
}
