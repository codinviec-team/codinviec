package com.project.codinviec_core_service.service;

import com.project.codinviec_core_service.dto.GroupCoreSkillDTO;
import com.project.codinviec_core_service.request.GroupCoreSkillRequest;
import com.project.codinviec_core_service.request.PageRequestCustom;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GroupCoreSkillService {
    List<GroupCoreSkillDTO> getAllGroupCoreSkill();
    Page<GroupCoreSkillDTO> getAllGroupCoreSkillPage(PageRequestCustom pageRequestCustom);
    GroupCoreSkillDTO getGroupCoreSkillById(Integer id);
    GroupCoreSkillDTO createGroupCoreSkill(GroupCoreSkillRequest request);
    GroupCoreSkillDTO updateGroupCoreSkill(int id, GroupCoreSkillRequest request);
    GroupCoreSkillDTO deleteGroupCoreSkill(int id);
}
