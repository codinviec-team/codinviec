package com.project.codinviec.repository;

import com.project.codinviec.entity.AvailableSkillsJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AvailableSkillsJobRepository extends JpaRepository<AvailableSkillsJob, Integer> {
    List<AvailableSkillsJob> findByJob_Id(int jobId);

    @Query("SELECT a FROM AvailableSkillsJob a JOIN FETCH a.availableSkill WHERE a.job.id IN :jobIds")
    List<AvailableSkillsJob> findByJob_IdIn(@Param("jobIds") List<Integer> jobIds);
}
