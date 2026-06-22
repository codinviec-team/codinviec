package com.project.codinviec_core_service.repository;

import com.project.codinviec_core_service.entity.StatusSpecial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface StatusSpecialRepository extends JpaRepository<StatusSpecial, Integer>, JpaSpecificationExecutor<StatusSpecial> {
}

