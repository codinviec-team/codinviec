package com.project.codinviec.service.imp.auth;

import com.project.codinviec.dto.InforEmailSecurityDTO;
import com.project.codinviec.dto.JobDTO;
import com.project.codinviec.dto.JobUserApplyDTO;
import com.project.codinviec.dto.auth.UserDTO;
import com.project.codinviec.entity.JobUser;
import com.project.codinviec.entity.auth.Company;
import com.project.codinviec.entity.auth.Role;
import com.project.codinviec.entity.auth.User;
import com.project.codinviec.event.payload.CreateUserCorePayload;
import com.project.codinviec.exception.auth.CreatedUserFail;
import com.project.codinviec.exception.auth.EmailAlreadyExistsExceptionHandler;
import com.project.codinviec.exception.common.ConflictExceptionHandler;
import com.project.codinviec.exception.common.NotFoundIdExceptionHandler;
import com.project.codinviec.mapper.AvailableSkillMapper;
import com.project.codinviec.mapper.JobMapper;
import com.project.codinviec.mapper.StatusSpecialMapper;
import com.project.codinviec.mapper.auth.UserMapper;
import com.project.codinviec.repository.AvailableSkillsJobRepository;
import com.project.codinviec.repository.JobUserRepository;
import com.project.codinviec.repository.StatusSpecialJobRepository;
import com.project.codinviec.repository.auth.CompanyRepository;
import com.project.codinviec.repository.auth.RoleRepository;
import com.project.codinviec.repository.auth.UserRepository;
import com.project.codinviec.request.PageRequestUser;
import com.project.codinviec.request.auth.SaveUserRequest;
import com.project.codinviec.request.auth.UpdateUserRequest;
import com.project.codinviec.service.auth.UserService;
import com.project.codinviec.specification.auth.UserSpecification;
import com.project.codinviec.util.helper.KafkaHelper;
import com.project.codinviec.util.helper.PageCustomHelper;
import com.project.codinviec.util.helper.TimeHelper;
import com.project.codinviec.util.security.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PageCustomHelper pageCustomHelper;
    private final UserSpecification userSpecification;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;
    private final PasswordGenerator passwordGenerator;
    private final KafkaHelper kafkaHelper;
    private final TimeHelper timeHelper;
    private final JobUserRepository JobUserRepository;
    private final JobMapper jobMapper;
    private final StatusSpecialMapper statusSpecialMapper;
    private final AvailableSkillMapper availableSkillMapper;
    private final StatusSpecialJobRepository statusSpecialJobRepository;
    private final AvailableSkillsJobRepository availableSkillsJobRepository;

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAllUserJoinFetch().stream()
                .map((user) ->
                   userMapper.userToUserDTO(user))
                .toList();
    }

    @Override
    public Page<UserDTO> getAllUsersPage(PageRequestUser pageRequestUser) {
        // Validate pageCustom
        PageRequestUser pageRequestValidate = pageCustomHelper.validatePageUser(pageRequestUser);

        // Tạo page cho api
        Pageable pageable = PageRequest.of(pageRequestValidate.getPageNumber() - 1, pageRequestValidate.getPageSize());

        // Tạo search
        Specification<User> spec = Specification.allOf(
                userSpecification.isBlocked(pageRequestUser.getBlock()),
                userSpecification.hasRoleId(pageRequestValidate.getRoleId()),
                userSpecification.searchByName(pageRequestValidate.getKeyword())
        );

        return userRepository.findAll(spec, pageable)
                .map((user) -> userMapper.userToUserDTO(user));
    }

    @Override
    public UserDTO getUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundIdExceptionHandler("Không tìm thấy id user"));
        UserDTO userDTO = userMapper.userToUserDTO(user);
        return userDTO;
    }

    @Override
    public UserDTO saveUser(SaveUserRequest saveUserRequest) {
        // check role
        Role role = null;
        if (saveUserRequest.getRoleId() != null && !saveUserRequest.getRoleId().isEmpty()) {
            role = roleRepository.findById(saveUserRequest.getRoleId()).orElseThrow(
                    () -> new NotFoundIdExceptionHandler("Không tìm thấy id role!"));
        }
        // check company
        Company company = null;
        if (saveUserRequest.getCompanyId() != null && !saveUserRequest.getCompanyId().isEmpty()) {
            company = companyRepository.findById(saveUserRequest.getCompanyId()).orElseThrow(
                    () -> new NotFoundIdExceptionHandler("Không tìm thấy id company!"));
        }

        User userCheckEmail = userRepository.findByEmail(saveUserRequest.getEmail()).orElse(null);
        if (userCheckEmail != null) {
            throw new EmailAlreadyExistsExceptionHandler("Email đã tồn tại!");
        }

        try {
            String password = passwordGenerator.generatePassword(12);

            System.out.println(password);

            User user = userMapper.saveUserMapper(role, company, saveUserRequest,password);
            User savedUser = userRepository.save(user);
            UserDTO userDTO = userMapper.userToUserDTO(savedUser);
            if (!savedUser.getId().isBlank() && !savedUser.getId().isEmpty() && savedUser.getId() != null) {
                kafkaHelper.sendKafkaEmailSecurity("create_user_email",
                        InforEmailSecurityDTO.builder()
                                .email(savedUser.getEmail())
                                .firstName(savedUser.getFirstName())
                                .password(password)
                                .dateCreated(timeHelper.parseLocalDateTimeToSimpleTime(savedUser.getCreatedDate()))
                                .build());
            }
            return userDTO;
        } catch (Exception e) {
            throw new ConflictExceptionHandler("Lỗi thêm user!");
        }
    }

    @Override
    public UserDTO updateUser(String idUser, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findById(idUser).orElseThrow(
                () -> new NotFoundIdExceptionHandler("Không tìm thấy id user!"));

        Role role = null;
        if (updateUserRequest.getRoleId() != null && !updateUserRequest.getRoleId().isEmpty()) {
            role = roleRepository.findById(updateUserRequest.getRoleId()).orElseThrow(
                    () -> new NotFoundIdExceptionHandler("Không tìm thấy id role!"));
        }

        Company company = null;
        if (updateUserRequest.getCompanyId() != null && !updateUserRequest.getCompanyId().isEmpty()) {
            company = companyRepository.findById(updateUserRequest.getCompanyId()).orElseThrow(
                    () -> new NotFoundIdExceptionHandler("Không tìm thấy id company!"));
        }

        try {
            User mappedUser = userMapper.updateUserMapper(idUser, role, company, updateUserRequest, user);
            User userSaved = userRepository.save(mappedUser);
            UserDTO userDTO = userMapper.userToUserDTO(userSaved);
            return userDTO;
        } catch (Exception e) {
            throw new ConflictExceptionHandler("Lỗi cập nhật user!");
        }
    }

    @Override
    @Transactional
    public UserDTO deleteUser(String idUser) {
        User user = userRepository.findById(idUser).orElseThrow(
                () -> new NotFoundIdExceptionHandler("Không tìm thấy id user!"));
        userRepository.delete(user);
        UserDTO userDTO = userMapper.userToUserDTO(user);
        return userDTO;
    }

    @Override
    public UserDTO blockUser(String idUser) {
        User user = userRepository.findById(idUser).orElseThrow(
                () -> new NotFoundIdExceptionHandler("Không tìm thấy id user!"));
        user.setIsBlock(Boolean.TRUE);
        User savedUser = userRepository.save(user);
        UserDTO userDTO = userMapper.userToUserDTO(savedUser);
        return userDTO;
    }

    @Override
    public UserDTO unblockUser(String idUser) {
        User user = userRepository.findById(idUser).orElseThrow(
                () -> new NotFoundIdExceptionHandler("Không tìm thấy id user!"));
        user.setIsBlock(Boolean.FALSE);
        User savedUser = userRepository.save(user);
        UserDTO userDTO = userMapper.userToUserDTO(savedUser);
        return userDTO;
    }

    @Override
    public JobUserApplyDTO getUserApplyByIdCompany(String idCompany) {
        Company company = companyRepository.findById(idCompany)
                .orElseThrow(() -> new NotFoundIdExceptionHandler("Không tìm thấy id company!"));
        List<JobUser> listJobUserApply = JobUserRepository.findByJob_Company(company);

        List<UserDTO> listUser = new ArrayList<>();
        List<JobDTO> listJobs =  new ArrayList<>();

        for (JobUser jobUser : listJobUserApply) {
            if (jobUser.getUser() != null){
                UserDTO userDTO = userMapper.userToUserDTO(jobUser.getUser());
                listUser.add(userDTO);
            }
            if (jobUser.getJob() != null){
                JobDTO jobDTO = jobMapper.toDTO(jobUser.getJob());
                jobDTO.setStatusSpecials(statusSpecialMapper
                        .StatusSpecialJobToStatusSpecialDTO(statusSpecialJobRepository
                                .findByJob_Id(jobUser.getJob().getId())));
                jobDTO.setSkills(availableSkillMapper
                        .AvailbleSkillJobToAvaibleSkill(
                                availableSkillsJobRepository.findByJob_Id(jobUser.getJob().getId())));
            }
        }
        return JobUserApplyDTO.builder().listUsers(listUser).listJobs(listJobs).build();
    }

    @Override
    @Transactional
    public String registeredUser(CreateUserCorePayload createUserCorePayload) {
        // check role
        Role role = null;
        if (createUserCorePayload.getRoleName() != null && !createUserCorePayload.getRoleName().isEmpty()) {
            role = roleRepository.findByRoleNameIgnoreCase(createUserCorePayload.getRoleName())
                    .orElseGet(() -> roleRepository.save(Role.builder()
                    .roleName("USER")
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .build()));
        }
        try {
            User user = userMapper.createdRegisterUserToUser(createUserCorePayload, role);
            userRepository.save(user);
            return "Đăng ký thành công!";
        }
        catch (DataIntegrityViolationException e) {
            return "Đăng ký thất bại!";
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new CreatedUserFail();
        }
    }
}
