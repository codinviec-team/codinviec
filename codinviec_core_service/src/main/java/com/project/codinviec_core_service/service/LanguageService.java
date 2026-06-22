package com.project.codinviec_core_service.service;

import com.project.codinviec_core_service.dto.LanguageDTO;
import com.project.codinviec_core_service.request.LanguageRequest;
import com.project.codinviec_core_service.request.PageRequestCustom;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LanguageService {
    List<LanguageDTO> getAllLanguage();
    Page<LanguageDTO> getAllLanguagePage(PageRequestCustom pageRequestCustom);
    LanguageDTO getLanguageById(Integer id);
    LanguageDTO createLanguage(LanguageRequest languageRequest);
    LanguageDTO updateLanguage(int id, LanguageRequest languageRequest);
    LanguageDTO deleteLanguage(int id);
}
