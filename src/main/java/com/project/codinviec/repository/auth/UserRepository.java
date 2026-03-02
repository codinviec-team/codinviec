package com.project.codinviec.repository.auth;

import com.project.codinviec.entity.auth.Company;
import com.project.codinviec.entity.auth.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);

    @Query("""
    select u
    from User u
    join fetch u.company
    join fetch u.role
""")
    List<User> findAllUserJoinFetch();

    @EntityGraph(attributePaths = {
            "role",
            "company",
    })
    Page<User> findAll(Specification<User> spec, Pageable pageable);
}
