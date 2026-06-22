package com.project.codinviec_core_service.service;

import com.project.codinviec_core_service.dto.SearchDTO;

public interface SearchService {
    SearchDTO getSearch(String keyword, int provinceId);
}
