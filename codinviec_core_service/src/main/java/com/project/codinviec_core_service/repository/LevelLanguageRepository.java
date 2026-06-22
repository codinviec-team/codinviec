package com.project.codinviec_core_service.repository;

import com.project.codinviec_core_service.entity.LevelLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LevelLanguageRepository extends JpaRepository<LevelLanguage, Integer> {
}
