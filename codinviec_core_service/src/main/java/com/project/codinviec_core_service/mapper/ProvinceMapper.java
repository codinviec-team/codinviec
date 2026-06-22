package com.project.codinviec_core_service.mapper;

import com.project.codinviec_core_service.dto.ProvinceCompanyDTO;
import com.project.codinviec_core_service.dto.ProvinceDTO;
import com.project.codinviec_core_service.entity.Province;
import com.project.codinviec_core_service.request.ProvinceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProvinceMapper {

    @Autowired
    private WardMapper wardMapper;

    public ProvinceDTO toDTO(Province entity) {
        if (entity == null)
            return null;
        return ProvinceDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .wards(entity.getWards() != null ? entity.getWards().stream()
                        .map(wardMapper::toDTOForProvince)
                        .collect(Collectors.toList()) : null)
                .build();
    }

    public ProvinceCompanyDTO toDTOCompany(Province entity) {
        if (entity == null)
            return null;
        return ProvinceCompanyDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public Province saveProvince(ProvinceRequest request) {
        if (request == null)
            return null;
        return Province.builder()
                .name(request.getName())
                .build();
    }

    public Province updateProvince(Integer id, ProvinceRequest request) {
        if (request == null)
            return null;
        return Province.builder()
                .id(id)
                .name(request.getName())
                .build();
    }
}
