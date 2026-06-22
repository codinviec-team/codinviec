package com.project.codinviec_core_service.repository;

import com.project.codinviec_core_service.entity.AvailableSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailableSkillRepository extends JpaRepository<AvailableSkill, Integer> {
}
