package com.project.codinviec_core_service.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LevelLanguageRequest {
    @NotBlank(message = "Tên cấp độ ngôn ngữ không được để trống")
    private String name;
}
