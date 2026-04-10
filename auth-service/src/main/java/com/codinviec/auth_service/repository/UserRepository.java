package com.codinviec.auth_service.repository;

import com.codinviec.auth_service.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,String> {
    Optional<UserEntity> findByEmail(String email);

    @Query("""
    select u
    from UserEntity u
    join fetch u.role
    where u.id = :id
""")
    Optional<UserEntity> findUserByIdWithRole(@Param("id") String id);
}
