package com.codinviec.auth_service.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ResendOtpRequest {
    @NotNull(message = "Email không được null!")
    @NotEmpty(message = "Email không được để trống!")
    private String email;
}
