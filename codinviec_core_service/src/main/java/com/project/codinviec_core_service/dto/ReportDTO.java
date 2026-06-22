package com.project.codinviec_core_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {
    private int id;
    private String title;
    private String description;
    private String imageUrl;
    private int statusId;
    private String createdReport;
    private String reportedUser;
    private int reportedJob;
}
