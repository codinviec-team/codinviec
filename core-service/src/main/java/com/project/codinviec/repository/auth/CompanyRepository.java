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
    @Query("""
    select c
    from Company c
    join fetch c.companySize
""")
    List<Company> findAllWithCompanySize();

    @EntityGraph(attributePaths = {
            "companySize"
    })
    Page<Company> findAll(Specification<Company> spec,Pageable pageable);
}
