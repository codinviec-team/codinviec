package com.project.codinviec_core_service.mapper;

import com.project.codinviec_core_service.dto.AwardDTO;
import com.project.codinviec_core_service.entity.Award;
import com.project.codinviec_core_service.entity.auth.User;
import com.project.codinviec_core_service.request.AwardRequest;
import org.springframework.stereotype.Component;

@Component
public class AwardMapper {

    public AwardDTO toDto(Award award) {
        if (award == null) return null;
        return AwardDTO.builder()
                .id(award.getId())
                .userId(award.getUser() != null ? award.getUser().getId() : null)
                .awardName(award.getAwardName())
                .organization(award.getOrganization())
                .date(award.getDate())
                .description(award.getDescription())
                .build();
    }

    public Award saveAward(User user, AwardRequest request) {
        if (request == null) return null;
        return Award.builder()
                .user(user)
                .awardName(request.getAwardName())
                .organization(request.getOrganization())
                .date(request.getDate())
                .description(request.getDescription())
                .build();
    }

    public Award updateAward(Integer id, User user, AwardRequest request) {
        if (request == null) return null;
        return Award.builder()
                .id(id)
                .user(user)
                .awardName(request.getAwardName())
                .organization(request.getOrganization())
                .date(request.getDate())
                .description(request.getDescription())
                .build();
    }
}
