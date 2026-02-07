package com.project.codinviec.mapper.auth;

import com.project.codinviec.dto.CompanyAddressDTO;
import com.project.codinviec.dto.CompanySizeDTO;
import com.project.codinviec.dto.StatusSpecialDTO;
import com.project.codinviec.dto.auth.CompanyDTO;
import com.project.codinviec.entity.CompanySize;
import com.project.codinviec.entity.auth.Company;
import com.project.codinviec.mapper.ProvinceMapper;
import com.project.codinviec.mapper.WardMapper;
import com.project.codinviec.request.auth.SaveUpdateCompanyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CompanyMapper {
    private final ProvinceMapper provinceMapper;
    private final WardMapper wardMapper;

    public CompanyDTO companyToCompanyDTO(Company company){
        CompanyDTO dto = CompanyDTO.builder()
                .id(company.getId())
                .name(company.getName())
                .description(company.getDescription())
                .website(company.getWebsite())
                .logo(company.getLogo())
                .createdDate(company.getCreatedDate())
                .updatedDate(company.getUpdatedDate())
                .build();
        // CompanySize
        if (company.getCompanySize() != null) {
            CompanySizeDTO sizeDTO = CompanySizeDTO.builder()
                    .id(company.getCompanySize().getId())
                    .minEmployees(company.getCompanySize().getMinEmployees())
                    .maxEmployees(company.getCompanySize().getMaxEmployees())
                    .build();
            dto.setCompanySize(sizeDTO);
        }
       return dto;
    }


    public CompanyDTO companyToCompanyDTOForAPIPage(Company company){
        return CompanyDTO.builder()
                .id(company.getId())
                .name(company.getName())
                .description(company.getDescription())
                .website(company.getWebsite())
                .logo(company.getLogo())
                .createdDate(company.getCreatedDate())
                .updatedDate(company.getUpdatedDate())
                .build();
    }

    public Company saveCompanyMapper( CompanySize companySize,SaveUpdateCompanyRequest saveUpdateCompanyRequest){

        return Company.builder()
                .id(UUID.randomUUID().toString())
                .name(saveUpdateCompanyRequest.getName())
                .description(saveUpdateCompanyRequest.getDescription())
                .logo(saveUpdateCompanyRequest.getLogo())
                .website(saveUpdateCompanyRequest.getWebsite())
                .companySize(companySize)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();
    }

    public Company updateCompanyMapper(String idCompany,CompanySize companySize ,SaveUpdateCompanyRequest saveUpdateCompanyRequest){
        return Company.builder()
                .id(idCompany)
                .name(saveUpdateCompanyRequest.getName())
                .description(saveUpdateCompanyRequest.getDescription())
                .logo(saveUpdateCompanyRequest.getLogo())
                .website(saveUpdateCompanyRequest.getWebsite())
                .companySize(companySize)
                .updatedDate(LocalDateTime.now())
                .build();
    }
}
