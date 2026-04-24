package com.project.codinviec.repository.auth;

import com.project.codinviec.entity.auth.Company;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company,String>, JpaSpecificationExecutor<Company> {
    @Override
    @EntityGraph(
            type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {"companySize", "industry"})
    List<Company> findAll();

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

}
