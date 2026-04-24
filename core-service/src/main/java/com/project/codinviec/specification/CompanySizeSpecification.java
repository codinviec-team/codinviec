package com.project.codinviec.specification;

import com.project.codinviec.entity.CompanySize;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CompanySizeSpecification {

    public Specification<CompanySize> searchByName(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) return cb.conjunction();
            String pattern = "%" + keyword.trim().toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.toString(root.get("minEmployees")), pattern),
                    cb.like(cb.toString(root.get("maxEmployees")), pattern)
            );
        };
    }
}
