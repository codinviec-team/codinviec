package com.project.codinviec_core_service.service;

import com.project.codinviec_core_service.dto.IndustryDTO;
import com.project.codinviec_core_service.request.IndustryRequest;
import com.project.codinviec_core_service.request.PageRequestCustom;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IndustryService {
    List<IndustryDTO> getAll();
    Page<IndustryDTO> getAllWithPage(PageRequestCustom req);
    IndustryDTO getById(int id);
    IndustryDTO create(IndustryRequest industry);
    IndustryDTO update(int id, IndustryRequest industry);
    void delete(int id);
}
