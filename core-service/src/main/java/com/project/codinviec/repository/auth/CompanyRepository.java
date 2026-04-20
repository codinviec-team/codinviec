package com.project.codinviec.repository.auth;

import com.project.codinviec.entity.auth.Company;
import jakarta.validation.constraints.NotNull;
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

@Repository
public interface CompanyRepository extends JpaRepository<Company,String>, JpaSpecificationExecutor<Company> {
    @EntityGraph(
            type =  EntityGraph.EntityGraphType.FETCH,
            attributePaths = {
                    "companySize",
                    "industry"
            })
    List<Company> findAll(Specification<Company> spec);

    @EntityGraph(
            type =  EntityGraph.EntityGraphType.FETCH,
            attributePaths = {
                    "companySize",
                    "industry"
            })
    List<Company> findByIsFeaturedTrue(Pageable pageable);

    @EntityGraph(
            type =  EntityGraph.EntityGraphType.FETCH,
            attributePaths = {
            "companySize",
            "industry"
    })
    Page<Company> findAll(Specification<Company> spec,Pageable pageable);
}
