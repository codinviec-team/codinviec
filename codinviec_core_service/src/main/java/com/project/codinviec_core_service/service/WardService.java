package com.project.codinviec_core_service.service;

import com.project.codinviec_core_service.dto.WardDTO;
import com.project.codinviec_core_service.request.PageRequestCustom;
import com.project.codinviec_core_service.request.WardRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface WardService {
    List<WardDTO> getAll();
    Page<WardDTO> getAllWithPage(PageRequestCustom req);
    WardDTO getById(int id);
    WardDTO create(WardRequest request);
    WardDTO update(int id, WardRequest request);
    void delete(int id);
}
