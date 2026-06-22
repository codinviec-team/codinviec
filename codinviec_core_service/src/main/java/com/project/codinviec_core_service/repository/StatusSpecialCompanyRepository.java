package com.project.codinviec_core_service.repository;

import com.project.codinviec_core_service.entity.StatusSpecialCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StatusSpecialCompanyRepository extends JpaRepository<StatusSpecialCompany, Integer> {
    List<StatusSpecialCompany> findByIdCompany_Id(String idCompanyId);
    @Query("""
    select ssc
    from StatusSpecialCompany ssc
    join fetch ssc.idStatusSpecial
    where ssc.idCompany.id in :companyIds
""")
    List<StatusSpecialCompany> findByCompanyIdsWithStatus(
            @Param("companyIds") List<String> companyIds
    );}
