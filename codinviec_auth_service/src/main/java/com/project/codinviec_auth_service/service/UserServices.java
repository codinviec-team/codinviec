package com.project.codinviec_auth_service.service;

import com.project.codinviec_auth_service.entity.UserEntity;

public interface UserServices {
    void deleteUser(String email);
    UserEntity getUserByEmail(String email);
    UserEntity saveUser(UserEntity user);
}
