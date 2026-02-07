package com.project.codinviec.repository;

import com.project.codinviec.entity.CompanyAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyAddressRepository extends JpaRepository<CompanyAddress, Integer>, JpaSpecificationExecutor<CompanyAddress> {
    List<CompanyAddress> findByCompany_Id(String companyId);
    @Query("""
    select ca
    from CompanyAddress ca
    join fetch ca.province
    join fetch ca.ward
    WHERE ca.company.id in :companyIds
""")
    List<CompanyAddress> findByCompanyIdsWithLocation(
            @Param("companyIds") List<String> companyIds
    );
}
