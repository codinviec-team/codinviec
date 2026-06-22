package com.project.codinviec_auth_service.repository;

import com.project.codinviec_auth_service.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity,String> {
    Optional<RoleEntity> findByRoleNameIgnoreCase(String roleName);
}
