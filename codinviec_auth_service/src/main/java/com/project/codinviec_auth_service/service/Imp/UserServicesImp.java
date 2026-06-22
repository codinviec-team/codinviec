package com.project.codinviec_auth_service.service.Imp;

import com.project.codinviec_auth_service.entity.UserEntity;
import com.project.codinviec_auth_service.exception.common.NotFoundIdExceptionHandler;
import com.project.codinviec_auth_service.repository.UserRepository;
import com.project.codinviec_auth_service.service.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServicesImp implements UserServices {
    private final UserRepository userRepository;
    @Override
    public void deleteUser(String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(
                ()->new NotFoundIdExceptionHandler("Không tìm thấy email")
        );
        userRepository.delete(user);
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                ()->new NotFoundIdExceptionHandler("Không tìm thấy email")
        );
    }

    @Override
    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }
}
