package com.codinviec.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccessTokenDTO {
    private String userId;
    private String token;
    private String expiryDate;
    private String createdDate;
}