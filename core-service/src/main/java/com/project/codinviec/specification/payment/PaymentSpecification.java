package com.project.codinviec.specification.payment;

import com.project.codinviec.entity.payment.Payment;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PaymentSpecification {

    public Specification<Payment> searchByTitle(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) return cb.conjunction();
            return cb.like(cb.lower(root.get("title")), "%" + keyword.trim().toLowerCase() + "%");
        };
    }
}
