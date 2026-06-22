package com.project.codinviec_core_service.repository;

import com.project.codinviec_core_service.entity.AvailableSkillExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailableSkillExperienceRepository extends JpaRepository<AvailableSkillExperience, Integer> {

    List<AvailableSkillExperience> findByUser_Id(String userId);
}
