package com.project.codinviec_core_service.service.auth;

import com.project.codinviec_core_service.dto.auth.CompanyDTO;
import com.project.codinviec_core_service.request.GetCompanyFeaturedRequest;
import com.project.codinviec_core_service.request.PageRequestCompany;
import com.project.codinviec_core_service.request.auth.SaveUpdateCompanyRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CompanyService {
    List<CompanyDTO> getAllCompany();
    List<CompanyDTO> getCompanyFeatured(GetCompanyFeaturedRequest getCompanyFeaturedRequest);
    Page<CompanyDTO> getAllCompanyPage(PageRequestCompany pageRequestCompany);
    CompanyDTO  getCompanyById(String idCompany);
    CompanyDTO saveCompany(SaveUpdateCompanyRequest saveUpdateCompanyRequest);
    CompanyDTO updateCompany(String idCompany ,SaveUpdateCompanyRequest saveUpdateCompanyRequest);
    CompanyDTO deleteCompany(String idCompany);
}
