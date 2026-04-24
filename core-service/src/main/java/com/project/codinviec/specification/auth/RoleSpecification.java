package com.project.codinviec.specification.auth;

import com.project.codinviec.entity.auth.Role;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class RoleSpecification {

    public Specification<Role> searchByName(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) return cb.conjunction();
            return cb.like(cb.lower(root.get("roleName")), "%" + keyword.trim().toLowerCase() + "%");
        };
    }
}
