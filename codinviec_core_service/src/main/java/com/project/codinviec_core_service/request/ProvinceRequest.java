package com.project.codinviec_core_service.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProvinceRequest {
    @NotBlank(message = "Tên tỉnh/thành phố không được để trống")
    private String name;
}

