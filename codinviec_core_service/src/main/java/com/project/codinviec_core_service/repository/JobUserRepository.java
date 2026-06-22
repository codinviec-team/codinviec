package com.project.codinviec_core_service.repository;

import com.project.codinviec_core_service.entity.JobUser;
import com.project.codinviec_core_service.entity.auth.Company;
import com.project.codinviec_core_service.entity.key.JobUserKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobUserRepository extends JpaRepository<JobUser, JobUserKey> {
    List<JobUser> findByJob_Company(Company company);
}
