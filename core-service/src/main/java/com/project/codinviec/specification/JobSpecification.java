package com.project.codinviec.specification;

import com.project.codinviec.entity.Job;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JobSpecification {

    public Specification<Job> searchByName(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) return cb.conjunction();
            try {
                int id = Integer.parseInt(keyword.trim());
                return cb.equal(root.get("id"), id);
            } catch (NumberFormatException e) {
                return cb.like(cb.lower(root.get("jobPosition")), "%" + keyword.trim().toLowerCase() + "%");
            }
        };
    }

    public Specification<Job> searchCompanyName(String companyName) {
        return (root, query, cb) -> {
            if (companyName == null || companyName.isBlank()) return cb.conjunction();
            Join<Object, Object> companyJoin = root.join("company", JoinType.INNER);
            return cb.like(cb.lower(companyJoin.get("name")), "%" + companyName.trim().toLowerCase() + "%");
        };
    }

    public Specification<Job> searchByNameAndProvinceId(String keyword, int provinceId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (provinceId != 0) {
                predicates.add(cb.equal(root.get("provinceId"), provinceId));
            }
            if (keyword != null && !keyword.trim().isEmpty()) {
                try {
                    int id = Integer.parseInt(keyword.trim());
                    predicates.add(cb.equal(root.get("jobPosition"), id));
                } catch (NumberFormatException e) {
                    predicates.add(cb.like(cb.lower(root.get("jobPosition")), "%" + keyword.trim().toLowerCase() + "%"));
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Specification<Job> filterJob(
            String provinceName,
            List<String> industryNames,
            List<String> jobLevelNames,
            List<String> employmentTypeNames,
            Double salaryMin,
            Double salaryMax
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (provinceName != null && !provinceName.isBlank()) {
                Join<Object, Object> provinceJoin = root.join("province", JoinType.INNER);
                predicates.add(cb.equal(cb.lower(provinceJoin.get("name")), provinceName.trim().toLowerCase()));
            }

            if (industryNames != null && !industryNames.isEmpty()) {
                Join<Object, Object> industryJoin = root.join("industry", JoinType.INNER);
                CriteriaBuilder.In<String> inClause = cb.in(industryJoin.get("name"));
                industryNames.forEach(inClause::value);
                predicates.add(inClause);
            }

            if (jobLevelNames != null && !jobLevelNames.isEmpty()) {
                Join<Object, Object> jobLevelJoin = root.join("jobLevel", JoinType.INNER);
                CriteriaBuilder.In<String> inClause = cb.in(jobLevelJoin.get("name"));
                jobLevelNames.forEach(inClause::value);
                predicates.add(inClause);
            }

            if (employmentTypeNames != null && !employmentTypeNames.isEmpty()) {
                Join<Object, Object> employmentJoin = root.join("employmentType", JoinType.INNER);
                CriteriaBuilder.In<String> inClause = cb.in(employmentJoin.get("name"));
                employmentTypeNames.forEach(inClause::value);
                predicates.add(inClause);
            }

            if (salaryMin != null && salaryMax != null) {
                predicates.add(cb.between(root.get("salary"), salaryMin, salaryMax));
            }

            query.distinct(true);
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
