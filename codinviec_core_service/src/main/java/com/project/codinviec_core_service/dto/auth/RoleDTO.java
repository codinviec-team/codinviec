package com.project.codinviec_core_service.dto.auth;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RoleDTO {
    private String id;
    private String roleName;
    private String description;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
