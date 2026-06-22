package com.project.codinviec_core_service.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmploymentTypeRequest {
    @NotBlank(message = "Tên loại hình công việc không được để trống")
    private String name;
}
