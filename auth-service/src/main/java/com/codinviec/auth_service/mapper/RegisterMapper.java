package com.codinviec.auth_service.mapper;


import com.codinviec.auth_service.dto.RegisterDTO;
import com.codinviec.auth_service.entity.RoleEntity;
import com.codinviec.auth_service.entity.UserEntity;
import com.codinviec.auth_service.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RegisterMapper {
    private final PasswordEncoder passwordEncoder;


    public RegisterDTO toRegisterDTO(UserEntity user) {
        return RegisterDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

    public UserEntity saveRegister(RegisterRequest request, RoleEntity role) {
        return UserEntity.builder()
                .email(request.getEmail().toLowerCase())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .isBlock(false)
                .role(role)
                .status("PENDING")
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();
    }
}
