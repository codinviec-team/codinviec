package com.project.codinviec_core_service.service;

import com.project.codinviec_core_service.dto.AvailableSkillDTO;
import com.project.codinviec_core_service.request.AvailableSkillRequest;
import com.project.codinviec_core_service.request.PageRequestCustom;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AvailableSkillService {
    List<AvailableSkillDTO> getAllAvailableSkill();
    Page<AvailableSkillDTO> getAllAvailableSkillPage(PageRequestCustom pageRequestCustom);
    AvailableSkillDTO getAvailableSkillById(Integer id);
    AvailableSkillDTO createAvailableSkill(AvailableSkillRequest request);
    AvailableSkillDTO updateAvailableSkill(int id, AvailableSkillRequest request);
    AvailableSkillDTO deleteAvailableSkill(int id);
}
