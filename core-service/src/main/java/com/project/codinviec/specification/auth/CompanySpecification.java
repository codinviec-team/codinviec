package com.project.codinviec.specification.auth;

import com.project.codinviec.entity.CompanyAddress;
import com.project.codinviec.entity.CompanySize;
import com.project.codinviec.entity.auth.Company;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CompanySpecification {

    public Specification<Company> fetchDetails() {
        return (root, query, cb) -> {
            if (Long.class != query.getResultType()) {
                root.fetch("companySize", JoinType.LEFT);
                root.fetch("industry", JoinType.LEFT);
            }
            return cb.conjunction();
        };
    }

    public Specification<Company> searchByName(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) return cb.conjunction();
            return cb.like(cb.lower(root.get("name")), "%" + keyword.trim().toLowerCase() + "%");
        };
    }

    public Specification<Company> minEmployees(Integer min) {
        return (root, query, cb) -> {
            if (min == null) return cb.conjunction();
            Join<Company, CompanySize> join = getOrCreateSizeJoin(root);
            return cb.greaterThanOrEqualTo(join.get("maxEmployees"), min);
        };
    }

    public Specification<Company> maxEmployees(Integer max) {
        return (root, query, cb) -> {
            if (max == null) return cb.conjunction();
            Join<Company, CompanySize> join = getOrCreateSizeJoin(root);
            return cb.lessThanOrEqualTo(join.get("minEmployees"), max);
        };
    }

    public Specification<Company> hasProvince(String provinceName) {
        return (root, query, cb) -> {
            if (provinceName == null || provinceName.trim().isEmpty()) return cb.conjunction();
            query.distinct(true);
            Join<Company, CompanyAddress> addressJoin = root.join("companyAddresses", JoinType.INNER);
            return cb.like(cb.lower(addressJoin.get("province").get("name")),
                    "%" + provinceName.trim().toLowerCase() + "%");
        };
    }

    public Specification<Company> isHeadOffice(Boolean headOffice) {
        return (root, query, cb) -> {
            if (headOffice == null) return cb.conjunction();
            Join<Company, CompanyAddress> addressJoin = root.join("companyAddresses", JoinType.INNER);
            return cb.equal(addressJoin.get("headOffice"), headOffice);
        };
    }

    @SuppressWarnings("unchecked")
    private Join<Company, CompanySize> getOrCreateSizeJoin(Root<Company> root) {
        for (Fetch<Company, ?> f : root.getFetches()) {
            if (f.getAttribute().getName().equals("companySize")) {
                return (Join<Company, CompanySize>) f;
            }
        }
        for (Join<Company, ?> j : root.getJoins()) {
            if (j.getAttribute().getName().equals("companySize")) {
                return (Join<Company, CompanySize>) j;
            }
        }
        return root.join("companySize", JoinType.LEFT);
    }
}
