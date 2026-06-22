package com.project.codinviec_core_service.mapper;

import com.project.codinviec_core_service.dto.CompanySizeDTO;
import com.project.codinviec_core_service.entity.CompanySize;
import com.project.codinviec_core_service.request.SaveUpdateCompanySizeRequest;
import org.springframework.stereotype.Component;

@Component
public class CompanySizeMapper {
    public CompanySizeDTO companySizeToCompanySizeDTO(CompanySize companySize) {
        if (companySize == null) return null;
        return CompanySizeDTO.builder()
                .id(companySize.getId())
                .minEmployees(companySize.getMinEmployees())
                .maxEmployees(companySize.getMaxEmployees())
                .build();
    }

    public CompanySize saveCompanySizeMapper(SaveUpdateCompanySizeRequest request) {
        return CompanySize.builder()
                .minEmployees(request.getMinEmployees())
                .maxEmployees(request.getMaxEmployees())
                .build();
    }

    public CompanySize updateCompanySizeMapper(Integer idCompanySize, SaveUpdateCompanySizeRequest request) {
        return CompanySize.builder()
                .id(idCompanySize)
                .minEmployees(request.getMinEmployees())
                .maxEmployees(request.getMaxEmployees())
                .build();
    }

}
