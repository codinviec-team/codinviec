package com.project.codinviec_core_service.mapper;

import com.project.codinviec_core_service.dto.AvailableSkillDTO;
import com.project.codinviec_core_service.entity.AvailableSkill;
import com.project.codinviec_core_service.entity.AvailableSkillsJob;
import com.project.codinviec_core_service.request.AvailableSkillRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AvailableSkillMapper {

    public AvailableSkillDTO toDto(AvailableSkill entity) {
        if (entity == null) return null;
        return AvailableSkillDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public AvailableSkill saveAvailableSkill(AvailableSkillRequest request) {
        if (request == null) return null;
        return AvailableSkill.builder()
                .name(request.getName())
                .build();
    }

    public AvailableSkill updateAvailableSkill(Integer id, AvailableSkillRequest request) {
        if (request == null) return null;
        return AvailableSkill.builder()
                .id(id)
                .name(request.getName())
                .build();
    }

    public List<AvailableSkillDTO> AvailbleSkillJobToAvaibleSkill(List<AvailableSkillsJob> availableSkillsJobs) {
        List<AvailableSkillDTO> listAvailableSkill = new ArrayList<>();
        for(AvailableSkillsJob item : availableSkillsJobs){
            listAvailableSkill.add(toDto(item.getAvailableSkill()));
        }
        return listAvailableSkill;
    }

}
