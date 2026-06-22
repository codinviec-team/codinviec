package com.project.codinviec_core_service.repository;

import com.project.codinviec_core_service.entity.JobLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JobLevelRepository extends JpaRepository<JobLevel, Integer>, JpaSpecificationExecutor<JobLevel> {
}
