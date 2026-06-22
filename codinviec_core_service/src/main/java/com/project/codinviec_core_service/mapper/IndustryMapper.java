package com.project.codinviec_core_service.mapper;

import com.project.codinviec_core_service.dto.IndustryDTO;
import com.project.codinviec_core_service.entity.Industry;
import com.project.codinviec_core_service.request.IndustryRequest;
import org.springframework.stereotype.Component;

@Component
public class IndustryMapper {
    public IndustryDTO toDTO(Industry industry) {
        if(industry == null) return null;
        return IndustryDTO.builder()
                .id(industry.getId())
                .name(industry.getName())
                .build();
    }

    public Industry saveIndustry(IndustryRequest request) {
        if(request == null) return null;
        return Industry.builder()
                .name(request.getName())
                .build();
    }

    public Industry updateIndustry(Integer id, IndustryRequest request) {
        if(request == null) return null;
        return Industry.builder()
                .id(id)
                .name(request.getName())
                .build();
    }
}
