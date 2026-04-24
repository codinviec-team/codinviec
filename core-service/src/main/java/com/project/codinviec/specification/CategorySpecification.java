package com.project.codinviec.specification;

import com.project.codinviec.entity.Category;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CategorySpecification {

    public Specification<Category> searchByName(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) return cb.conjunction();
            return cb.like(cb.lower(root.get("name")), "%" + keyword.trim().toLowerCase() + "%");
        };
    }

    public Specification<Category> parentIsNull() {
        return (root, query, cb) -> cb.isNull(root.get("parent"));
    }
}
