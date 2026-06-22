package com.project.codinviec_core_service.service;

import com.project.codinviec_core_service.dto.DegreeLevelDTO;
import com.project.codinviec_core_service.request.DegreeLevelRequest;
import com.project.codinviec_core_service.request.PageRequestCustom;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DegreeLevelService {
    List<DegreeLevelDTO> getAll();
    Page<DegreeLevelDTO> getAllWithPage(PageRequestCustom req);
    DegreeLevelDTO getById(int id);
    DegreeLevelDTO create(DegreeLevelRequest request);
    DegreeLevelDTO update(int id, DegreeLevelRequest request);
    void delete(int id);
}
