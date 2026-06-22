package com.project.codinviec_core_service.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceRequest {
    @NotBlank(message = "Tên kinh nghiệm không được để trống")
    private String name;
}
