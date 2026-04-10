package com.codinviec.auth_service.event.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterEmailPayload {
    private String email;
    private String firstName;
    private String lastName;
}
