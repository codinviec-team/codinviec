package com.project.codinviec.specification;

import com.project.codinviec.entity.Blog;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class BlogSpecification {
    public Specification<Blog> searchByName(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isEmpty()) return cb.conjunction();
            return cb.like(cb.lower(root.get("title")), "%" + keyword.toLowerCase() + "%");
        };
    }
}
