package com.codinviec.auth_service.service;

import com.codinviec.auth_service.entity.UserEntity;

public interface UserServices {
    void deleteUser(String email);
    UserEntity getUserByEmail(String email);
    UserEntity saveUser(UserEntity user);
}
