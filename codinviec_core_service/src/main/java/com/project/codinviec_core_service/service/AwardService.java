package com.project.codinviec_core_service.service;

import com.project.codinviec_core_service.dto.AwardDTO;
import com.project.codinviec_core_service.request.AwardRequest;
import com.project.codinviec_core_service.request.PageRequestCustom;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AwardService {
    List<AwardDTO> getAllAward();
    Page<AwardDTO> getAllAwardPage(PageRequestCustom pageRequestCustom);
    List<AwardDTO> getAwardByUser(String userId);
    AwardDTO getAwardById(Integer id);
    AwardDTO createAward(AwardRequest request);
    AwardDTO updateAward(int id, AwardRequest request);
    AwardDTO deleteAward(int id);
}
