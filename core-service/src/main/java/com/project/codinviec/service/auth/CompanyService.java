package com.project.codinviec.service.auth;

import com.project.codinviec.dto.auth.CompanyDTO;
import com.project.codinviec.request.GetCompanyFeaturedRequest;
import com.project.codinviec.request.PageRequestCompany;
import com.project.codinviec.request.auth.SaveUpdateCompanyRequest;
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
