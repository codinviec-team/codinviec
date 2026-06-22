package com.project.codinviec_core_service.service;

import com.project.codinviec_core_service.dto.LevelLanguageDTO;
import com.project.codinviec_core_service.request.LevelLanguageRequest;
import com.project.codinviec_core_service.request.PageRequestCustom;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LevelLanguageService {
    List<LevelLanguageDTO> getAllLevelLanguage();
    Page<LevelLanguageDTO> getAllLevelLanguagePage(PageRequestCustom pageRequestCustom);
    LevelLanguageDTO getLevelLanguageById(Integer id);
    LevelLanguageDTO createLevelLanguage(LevelLanguageRequest levelLanguageRequest);
    LevelLanguageDTO updateLevelLanguage(int id, LevelLanguageRequest levelLanguageRequest);
    LevelLanguageDTO deleteLevelLanguage(int id);
}
