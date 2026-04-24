package com.project.codinviec.specification;

import com.project.codinviec.entity.CompanyAddress;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CompanyAddressSpeciification {

    public Specification<CompanyAddress> searchById(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) return cb.conjunction();
            try {
                return cb.equal(root.get("id"), Integer.parseInt(keyword.trim()));
            } catch (NumberFormatException e) {
                return cb.conjunction();
            }
        };
    }

    public Specification<CompanyAddress> filterByProvinceId(String provinceId) {
        return (root, query, cb) -> {
            if (provinceId == null || provinceId.trim().isEmpty()) return cb.conjunction();
            try {
                return cb.equal(root.get("province").get("id"), Integer.parseInt(provinceId.trim()));
            } catch (NumberFormatException e) {
                return cb.conjunction();
            }
        };
    }

    public Specification<CompanyAddress> filterByWardId(String wardId) {
        return (root, query, cb) -> {
            if (wardId == null || wardId.trim().isEmpty()) return cb.conjunction();
            try {
                return cb.equal(root.get("ward").get("id"), Integer.parseInt(wardId.trim()));
            } catch (NumberFormatException e) {
                return cb.conjunction();
            }
        };
    }
}
