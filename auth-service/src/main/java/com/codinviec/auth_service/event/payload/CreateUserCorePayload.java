package com.codinviec.auth_service.event.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserCorePayload {
    private String id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Boolean  isBlock;
    private String avatar;
    private String status;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    private String roleName;
}
