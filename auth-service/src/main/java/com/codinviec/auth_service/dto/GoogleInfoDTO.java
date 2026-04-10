package com.codinviec.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoogleInfoDTO {
    private String googleId;
    private String email;
    private String firstName;
    private String lastName;
    private String picture;
}
