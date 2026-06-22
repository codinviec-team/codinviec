package com.project.codinviec_core_service.mapper;

import com.project.codinviec_core_service.dto.CertificateDTO;
import com.project.codinviec_core_service.entity.Certificate;
import com.project.codinviec_core_service.entity.auth.User;
import com.project.codinviec_core_service.request.CertificateRequest;
import org.springframework.stereotype.Component;

@Component
public class CertificateMapper {

    public CertificateDTO toDto(Certificate certificate) {
        if (certificate == null) return null;
        return CertificateDTO.builder()
                .id(certificate.getId())
                .userId(certificate.getUser() != null ? certificate.getUser().getId() : null)
                .certificateName(certificate.getCertificateName())
                .organization(certificate.getOrganization())
                .date(certificate.getDate())
                .link(certificate.getLink())
                .description(certificate.getDescription())
                .build();
    }

    public Certificate saveCertificate(User user, CertificateRequest request) {
        if (request == null) return null;
        return Certificate.builder()
                .user(user)
                .certificateName(request.getCertificateName())
                .organization(request.getOrganization())
                .date(request.getDate())
                .link(request.getLink())
                .description(request.getDescription())
                .build();
    }

    public Certificate updateCertificate(Integer id, User user, CertificateRequest request) {
        if (request == null) return null;
        return Certificate.builder()
                .id(id)
                .user(user)
                .certificateName(request.getCertificateName())
                .organization(request.getOrganization())
                .date(request.getDate())
                .link(request.getLink())
                .description(request.getDescription())
                .build();
    }
}
