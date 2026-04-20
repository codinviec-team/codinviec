package com.project.codinviec.repository;

import com.project.codinviec.entity.StatusSpecialJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StatusSpecialJobRepository extends JpaRepository<StatusSpecialJob, Integer> {
    List<StatusSpecialJob> findByJob_Id(int jobId);

    @Query("SELECT s FROM StatusSpecialJob s JOIN FETCH s.statusSpecial WHERE s.job.id IN :jobIds")
    List<StatusSpecialJob> findByJob_IdIn(@Param("jobIds") List<Integer> jobIds);
}
