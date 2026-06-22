package com.project.codinviec_core_service.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class ReportStatusRequest {
    @NotBlank(message = "Tên trạng thái báo cáo không được để trống")
    private String name;
}
