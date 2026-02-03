package com.project.codinviec.repository.auth;

import com.project.codinviec.entity.auth.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);
    @EntityGraph(attributePaths = {
            "role",
            "company",
            "company.companySize",
            "company.companyAddresses"
    })
    List<User> findAll();
}
