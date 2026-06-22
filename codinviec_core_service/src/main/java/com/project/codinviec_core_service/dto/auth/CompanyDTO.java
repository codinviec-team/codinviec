package com.project.codinviec_core_service.dto.auth;

import com.project.codinviec_core_service.dto.CompanyAddressDTO;
import com.project.codinviec_core_service.dto.CompanySizeDTO;
import com.project.codinviec_core_service.dto.IndustryDTO;
import com.project.codinviec_core_service.dto.StatusSpecialDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyDTO {
    private String id;
    private String name;
    private String description;
    private String website;
    private String logo;
    private List<StatusSpecialDTO> statusSpecials;
    private CompanySizeDTO  companySize;
    private List<CompanyAddressDTO> companyAddress = new ArrayList<>();
    private Boolean isFeatured;
    private int JobActive;
    private IndustryDTO industry;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
