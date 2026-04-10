package com.codinviec.auth_service.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtUserDTO {
    private String userId;
    private String role;
    private Integer tokenVersion;
    private String deviceId;
}
