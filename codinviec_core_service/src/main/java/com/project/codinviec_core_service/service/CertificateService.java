package com.project.codinviec_core_service.service;

import com.project.codinviec_core_service.dto.CertificateDTO;
import com.project.codinviec_core_service.request.CertificateRequest;
import com.project.codinviec_core_service.request.PageRequestCustom;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CertificateService {
    List<CertificateDTO> getAllCertificate();
    Page<CertificateDTO> getAllCertificatePage(PageRequestCustom pageRequestCustom);
    List<CertificateDTO> getCertificateByUser(String userId);
    CertificateDTO getCertificateById(Integer id);
    CertificateDTO createCertificate(CertificateRequest request);
    CertificateDTO updateCertificate(int id, CertificateRequest request);
    CertificateDTO deleteCertificate(int id);
}
