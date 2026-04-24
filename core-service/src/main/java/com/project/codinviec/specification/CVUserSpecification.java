package com.project.codinviec.specification;

import com.project.codinviec.entity.CVUser;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CVUserSpecification {

    public Specification<CVUser> searchByTitle(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) return cb.conjunction();
            return cb.like(cb.lower(root.get("title")), "%" + keyword.trim().toLowerCase() + "%");
        };
    }
}
