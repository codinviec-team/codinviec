package com.project.codinviec_core_service.service;

import com.project.codinviec_core_service.dto.JobDTO;
import com.project.codinviec_core_service.dto.StatusSpecialDTO;
import com.project.codinviec_core_service.request.PageRequestCustom;
import com.project.codinviec_core_service.request.StatusSpecialRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StatusSpecialService {
    List<StatusSpecialDTO> getAll();
    Page<StatusSpecialDTO> getAllWithPage(PageRequestCustom req);
    StatusSpecialDTO getById(int id);
    StatusSpecialDTO create(StatusSpecialRequest request);
    StatusSpecialDTO update(int id, StatusSpecialRequest request);
    StatusSpecialDTO delete(int id);
}

