package com.project.codinviec_core_service.service;

import com.project.codinviec_core_service.dto.LanguageSkillDTO;
import com.project.codinviec_core_service.request.LanguageSkillRequest;
import com.project.codinviec_core_service.request.PageRequestCustom;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LanguageSkillService {
    List<LanguageSkillDTO> getAllLanguageSkill();
    Page<LanguageSkillDTO> getAllLanguageSkillPage(PageRequestCustom pageRequestCustom);
    List<LanguageSkillDTO> getLanguageSkillByUser(String userId);
    LanguageSkillDTO getLanguageSkillById(Integer id);
    LanguageSkillDTO createLanguageSkill(LanguageSkillRequest request);
    LanguageSkillDTO updateLanguageSkill(int id, LanguageSkillRequest request);
    LanguageSkillDTO deleteLanguageSkill(int id);
}
