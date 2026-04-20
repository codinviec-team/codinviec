package com.project.codinviec.repository;

import com.project.codinviec.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer>, JpaSpecificationExecutor<Job> {

    @EntityGraph("Job.withAssociations")
    List<Job> findAll();

    @EntityGraph("Job.withAssociations")
    Page<Job> findAll(Specification<Job> spec, Pageable pageable);

    @EntityGraph("Job.withAssociations")
    Optional<Job> findById(Integer id);

    @EntityGraph("Job.withAssociations")
    List<Job> getJobByCompany_Id(String companyId);

    @Query("""
        SELECT j.company.id, COUNT(j)
        FROM job j
        WHERE j.company.id IN :companyIds
        GROUP BY j.company.id
    """)
    List<Object[]> countJobByCompanyIds(@Param("companyIds") List<String> companyIds);

    @Query(value = "SELECT id FROM job WHERE is_featured = true ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Integer> findFeaturedJobIdsRandom(@Param("limit") int limit);

    @EntityGraph("Job.withAssociations")
    @Query("SELECT j FROM job j WHERE j.id IN :ids")
    List<Job> findByIdInWithAssociations(@Param("ids") List<Integer> ids);
}
