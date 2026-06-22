package com.project.codinviec_core_service.mapper;

import com.project.codinviec_core_service.dto.LevelLanguageDTO;
import com.project.codinviec_core_service.entity.LevelLanguage;
import com.project.codinviec_core_service.request.LevelLanguageRequest;
import org.springframework.stereotype.Component;

@Component
public class LevelLanguageMapper {
    public LevelLanguageDTO toDto(LevelLanguage levelLanguage) {
        if (levelLanguage == null) return null;
        return LevelLanguageDTO.builder()
                .id(levelLanguage.getId())
                .name(levelLanguage.getName())
                .build();
    }

    public LevelLanguage saveLevelLanguage(LevelLanguageRequest request) {
        if (request == null) return null;
        return LevelLanguage.builder()
                .name(request.getName())
                .build();
    }

    public LevelLanguage updateLevelLanguage(Integer id, LevelLanguageRequest request) {
        if (request == null) return null;
        return LevelLanguage.builder()
                .id(id)
                .name(request.getName())
                .build();
    }
}
