package com.project.codinviec_core_service.repository;

import com.project.codinviec_core_service.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Integer> {
    List<Certificate> findByUser_Id(String userId);
}
