package com.project.codinviec_core_service.service.auth;

import com.project.codinviec_core_service.dto.JobUserApplyDTO;
import com.project.codinviec_core_service.dto.auth.UserDTO;
import com.project.codinviec_core_service.event.payload.CreateUserCorePayload;
import com.project.codinviec_core_service.request.PageRequestUser;
import com.project.codinviec_core_service.request.auth.SaveUserRequest;
import com.project.codinviec_core_service.request.auth.UpdateUserRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();
    Page<UserDTO> getAllUsersPage(PageRequestUser pageRequestUser);
    UserDTO getUserById(String id);
    UserDTO getProfileUserById(String id);
    UserDTO saveUser(SaveUserRequest saveUserRequest);
    UserDTO updateUser(String idUser, UpdateUserRequest updateUserRequest);
    UserDTO deleteUser(String idUser);
    UserDTO blockUser(String idUser);
    UserDTO unblockUser(String idUser);
    JobUserApplyDTO getUserApplyByIdCompany(String idCompany);
    String registeredUser(CreateUserCorePayload createUserCorePayload);
}
