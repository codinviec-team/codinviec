package com.project.codinviec.specification.payment;

import com.project.codinviec.entity.payment.PaymentMethod;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodSpecification {

    public Specification<PaymentMethod> searchByName(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) return cb.conjunction();
            return cb.like(cb.lower(root.get("name")), "%" + keyword.trim().toLowerCase() + "%");
        };
    }
}
