package com.project.codinviec_core_service.service;

import com.project.codinviec_core_service.dto.AvailableSkillExperienceDTO;
import com.project.codinviec_core_service.request.AvailableSkillExperienceRequest;
import com.project.codinviec_core_service.request.DeleteAvailableSkillExperienceByGroupCoreIdRequest;
import com.project.codinviec_core_service.request.PageRequestCustom;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AvailableSkillExperienceService {

    List<AvailableSkillExperienceDTO> getAllAvailableSkillExperience();

    Page<AvailableSkillExperienceDTO> getAllAvailableSkillExperiencePage(PageRequestCustom pageRequestCustom);

    List<AvailableSkillExperienceDTO> getAvailableSkillExperienceByUser(String userId);

    AvailableSkillExperienceDTO getAvailableSkillExperienceById(Integer id);

    AvailableSkillExperienceDTO createAvailableSkillExperience(AvailableSkillExperienceRequest request);

    AvailableSkillExperienceDTO updateAvailableSkillExperience(int id, AvailableSkillExperienceRequest request);

    AvailableSkillExperienceDTO deleteAvailableSkillExperience(int id);
    List<AvailableSkillExperienceDTO> deleteAvailableSkillExperienceByGroupCoreId( DeleteAvailableSkillExperienceByGroupCoreIdRequest request);

}
