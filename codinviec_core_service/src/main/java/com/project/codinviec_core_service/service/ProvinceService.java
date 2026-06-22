package com.project.codinviec_core_service.service;

import com.project.codinviec_core_service.dto.ProvinceDTO;
import com.project.codinviec_core_service.request.PageRequestCustom;
import com.project.codinviec_core_service.request.ProvinceRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProvinceService {
    List<ProvinceDTO> getAll();
    Page<ProvinceDTO> getAllWithPage(PageRequestCustom req);
    ProvinceDTO getById(int id);
    ProvinceDTO create(ProvinceRequest request);
    ProvinceDTO update(int id, ProvinceRequest request);
    void delete(int id);
}
