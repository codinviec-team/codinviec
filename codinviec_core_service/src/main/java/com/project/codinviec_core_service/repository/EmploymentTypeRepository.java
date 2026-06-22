package com.project.codinviec_core_service.repository;

import com.project.codinviec_core_service.entity.EmploymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EmploymentTypeRepository extends JpaRepository<EmploymentType, Integer>, JpaSpecificationExecutor<EmploymentType> {
}
