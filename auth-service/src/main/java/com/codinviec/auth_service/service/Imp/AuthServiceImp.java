package com.codinviec.auth_service.service.Imp;

import com.codinviec.auth_service.dto.GoogleInfoDTO;
import com.codinviec.auth_service.dto.JwtUserDTO;
import com.codinviec.auth_service.dto.TokenDTO;
import com.codinviec.auth_service.dto.VerifyUserDTO;
import com.codinviec.auth_service.entity.RoleEntity;
import com.codinviec.auth_service.entity.UserEntity;
import com.codinviec.auth_service.event.payload.CreateUserCorePayload;
import com.codinviec.auth_service.event.payload.UserRegisterEmailPayload;
import com.codinviec.auth_service.event.publish.AuthEventPublisher;
import com.codinviec.auth_service.exception.event.ResendVerifyOtpOverCounter;
import com.codinviec.auth_service.exception.event.ResendVerifyOtpUserFail;
import com.codinviec.auth_service.exception.event.SendVerifyOtpUserFail;
import com.codinviec.auth_service.exception.event.VerifyOtpFail;
import com.codinviec.auth_service.exception.security.*;
import com.codinviec.auth_service.mapper.RegisterMapper;
import com.codinviec.auth_service.repository.RoleRepository;
import com.codinviec.auth_service.repository.UserRepository;
import com.codinviec.auth_service.request.*;
import com.codinviec.auth_service.service.AuthService;
import com.codinviec.auth_service.service.DeviceSessionService;
import com.codinviec.auth_service.util.JWTHepler;
import com.codinviec.auth_service.util.TimeHelper;
import com.codinviec.auth_service.util.VerifyUserHelper;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {
    @Value("${google.client-id}")
    private String clientId;

    @Value("${google.redirect-uri}")
    private String redirectUri;

    @Value("${google.client-secret}")
    private String clientSecret;


    @Qualifier("redisTemplateDb0")
    private final RedisTemplate<String, String> redisTemplateDb;

    private final ObjectMapper objectMapper;
    private final TimeHelper timeHelper;
    private final JWTHepler jwtHealper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RegisterMapper registerMapper;
    private final RoleRepository roleRepository;
    private final VerifyUserHelper verifyUserHelper;
    private final AuthEventPublisher authEventPublisher;
    private final DeviceSessionService deviceSessionService;

    /***
     * Key redis note
     */
//    token:refresh:{userId}:{user_devices} = refreshToken
    private final String keyRefreshTokenRedis = "token:refresh:";
    //    token:version:{userId} = giá trị version token
    private final String keyVersionRedis = "token:version:";
    //    otp key
    private final String keyOtpUser = "register:otp:";


    @Override
    public TokenDTO login(LoginRequest loginRequest) {
        UserEntity user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> {
                    // điếm số lần nhập sai
                    return new WrongPasswordOrEmailExceptionHandler("Tài khoản hoặc mật khẩu không hợp lệ!");
                });

//        check pass
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new WrongPasswordOrEmailExceptionHandler("Tài khoản hoặc mật khẩu không hợp lệ!");
        }

//        check block
        if (user.getIsBlock()) {
            throw new BlockLoginUserExceptionHandler("Tài khoản đã bị khóa hãy liên hệ admin!");
        }

        //        xử lí tokenVersion
        int tokenVersion = 1;
        if (redisTemplateDb.hasKey(keyVersionRedis + user.getId())) {
            tokenVersion = Integer.parseInt(Objects.requireNonNull(redisTemplateDb.opsForValue().get(keyVersionRedis + user.getId())));
        } else {
            redisTemplateDb.opsForValue().set(keyVersionRedis + user.getId(), String.valueOf(tokenVersion));
        }

        String accessToken = jwtHealper.createAccessToken(user.getRole().getRoleName(), tokenVersion, user.getId(), loginRequest.getDevicesId());
        String refershToken = jwtHealper.createRefreshToken(user.getRole().getRoleName(), user.getId(), keyRefreshTokenRedis, tokenVersion, loginRequest.getDevicesId());

        deviceSessionService.registerDevice(user.getId(), loginRequest.getDevicesId(), keyRefreshTokenRedis);

        return TokenDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refershToken)
                .build();
    }

    @Override
    @Transactional
    public String register(RegisterRequest registerRequest) {
        userRepository.findByEmail(registerRequest.getEmail())
                .ifPresent(user -> {
                    throw new EmailAlreadyExistsExceptionHandler("Email đã tồn tại!");
                });

        RoleEntity defaultRole = roleRepository.findByRoleNameIgnoreCase("USER")
                .orElseGet(() -> roleRepository.save(RoleEntity.builder()
                        .roleName("USER")
                        .createdDate(LocalDateTime.now())
                        .updatedDate(LocalDateTime.now())
                        .build()));

        UserEntity user = registerMapper.saveRegister(registerRequest, defaultRole);
        UserEntity savedUser = userRepository.save(user);

//        publish to Notification
        if (!savedUser.getId().isBlank() && !savedUser.getId().isEmpty() && savedUser.getId() != null) {
            try {
                verifyUserHelper.sendOtpUserEmail(savedUser.getEmail(), 0, keyOtpUser);
            } catch (Exception e) {
                throw new SendVerifyOtpUserFail();
            }
        }
        return "Vui lòng xác thực tài khoản!";
    }

    @Override
    @Transactional
    public TokenDTO refreshToken(RefreshTokenRequest refreshTokenRequest, HttpServletResponse response) {
        JwtUserDTO userJwt = jwtHealper.verifyRefreshToken(refreshTokenRequest.getRefreshToken(), keyRefreshTokenRedis, keyVersionRedis, response);
        String accessToken = jwtHealper.createAccessToken(userJwt.getRole(), userJwt.getTokenVersion(), userJwt.getUserId(), userJwt.getDeviceId());
        String refershToken = jwtHealper.createRefreshToken(userJwt.getRole(), userJwt.getUserId(), keyRefreshTokenRedis, userJwt.getTokenVersion(), userJwt.getDeviceId());
        return TokenDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refershToken)
                .build();
    }

    @Transactional
    @Override
    public void logout(LogoutRequest logoutRequest, HttpServletResponse response) {
        JwtUserDTO userJwt = jwtHealper.verifyRefreshToken(logoutRequest.getRefreshToken(), keyRefreshTokenRedis, keyVersionRedis, response);
        deviceSessionService.logoutDevice(userJwt.getUserId(), userJwt.getDeviceId());
        jwtHealper.revokeAllTokens(userJwt.getUserId(), userJwt.getDeviceId(), keyRefreshTokenRedis, response);
    }

    @Override
    public void resendOtp(ResendOtpRequest resendOtpRequest) {
        try {
            UserEntity user = userRepository.findByEmail(resendOtpRequest.getEmail()).orElseThrow(
                    UnregisteredUsers::new
            );
            if (redisTemplateDb.hasKey(keyVersionRedis + user.getId())) {
                String json = redisTemplateDb.opsForValue().get(user.getEmail());
                VerifyUserDTO verifyUserDTO = objectMapper.readValue(json, VerifyUserDTO.class);
                if (verifyUserDTO.getCounterResend() <= 5) {
                    verifyUserHelper.sendOtpUserEmail(user.getEmail(), 0, keyOtpUser);
                } else {
                    throw new ResendVerifyOtpOverCounter();
                }
            } else {
                verifyUserHelper.sendOtpUserEmail(user.getEmail(), 0, keyOtpUser);
            }
        } catch (Exception e) {
            throw new ResendVerifyOtpUserFail();
        }

    }

    @Override
    @Transactional
    public void verifyUserOtp(VerifyUserRequest verifyUserRequest) {
        try {
            UserEntity user = userRepository.findByEmail(verifyUserRequest.getEmail()).orElseThrow(
                    () -> new UnregisteredUsers()
            );
            if (redisTemplateDb.hasKey(keyOtpUser + user.getEmail())) {

                String json = redisTemplateDb.opsForValue().get(keyOtpUser + user.getEmail());
                VerifyUserDTO verifyUserDTO = objectMapper.readValue(json, VerifyUserDTO.class);
                if (verifyUserDTO.getOtp().equalsIgnoreCase(verifyUserRequest.getOtp())) {

//                   Gửi sự kiện cho codinviec core tạo user
                    authEventPublisher.publishUserRegisteredSuccess(CreateUserCorePayload.builder()
                            .id(user.getId())
                            .avatar(user.getAvatar())
                            .email(user.getEmail())
                            .password(user.getPassword())
                            .firstName(user.getFirstName())
                            .lastName(user.getLastName())
                            .isBlock(user.getIsBlock())
                            .status(user.getStatus())
                            .createdDate(user.getCreatedDate())
                            .updatedDate(user.getUpdatedDate())
                            .roleName(user.getRole().getRoleName())
                            .build());
                } else {
                    throw new VerifyOtpFail();
                }
            } else {
                throw new VerifyOtpFail();
            }
        } catch (UnregisteredUsers unregisteredUsers) {
            throw new UnregisteredUsers();
        } catch (VerifyOtpFail verifyOtpFail) {
            throw new VerifyOtpFail();
        } catch (Exception e) {
            throw new VerifyOtpFail();
        }
    }

    @Override
    public TokenDTO loginGoogleHandler(String code) {
        try {
//         Đổi token từ code trả về để có token google lấy thông tin user
//            Json khi call về
//      {
//          "access_token": "ya29.a0...",
//          "expires_in": 3599,
//          "token_type": "Bearer",
//          "id_token": "eyJhbGciOiJSUzI1NiIs..."
//      }
            GoogleTokenResponse tokenResponse =
                    new GoogleAuthorizationCodeTokenRequest(
                            new NetHttpTransport(),
                            JacksonFactory.getDefaultInstance(),
                            clientId,
                            clientSecret,
                            code,
                            redirectUri
                    ).execute();
//         Chuẩn bị đường dẫn để call api google tiếp
            HttpRequestFactory factory =
                    new NetHttpTransport().createRequestFactory();
            GenericUrl url =
                    new GenericUrl("https://www.googleapis.com/oauth2/v2/userinfo");
            HttpRequest request = factory.buildGetRequest(url);

//           Gắn token google và đường dẫn call api
            request.getHeaders().setAuthorization("Bearer " + tokenResponse.getAccessToken());

//           Call thông tin user đăng nhập từ google về
            String response = request.execute().parseAsString();
            JsonNode node = objectMapper.readTree(response);
//           JSON google trả về
//            {
//                      "id": "10923819283",
//                    "email": "abc@gmail.com",
//                    "verified_email": true,
//                    "name": "Nguyen Van A",
//                    "given_name": "Nguyen",
//                    "family_name": "Van A",
//                    "picture": "https://lh3.googleusercontent.com/..."
//            }
            GoogleInfoDTO googleInfoDTO = GoogleInfoDTO.builder()
                    .googleId(node.path("id").asText() != null ? node.path("id").asText() : null)
                    .email(node.get("email").asText() != null ? node.get("email").asText() : null)
                    .lastName(node.get("given_name").asText() != null ? node.get("given_name").asText() : null)
                    .firstName(node.get("family_name").asText() != null ? node.get("family_name").asText() : null)
                    .picture(node.get("picture").asText() != null ? node.get("picture").asText() : null)
                    .build();

            UserEntity user = userRepository.findByEmail(googleInfoDTO.getEmail()).orElse(null);
            if (user == null) {
                // Nếu user chưa tồn tại, tạo user mới
                RoleEntity defaultRole = roleRepository.findByRoleNameIgnoreCase("USER")
                        .orElseGet(() -> roleRepository.save(RoleEntity.builder()
                                .roleName("USER")
                                .createdDate(LocalDateTime.now())
                                .updatedDate(LocalDateTime.now())
                                .build()));

                user = UserEntity.builder()
                        .email(googleInfoDTO.getEmail())
                        .firstName(googleInfoDTO.getFirstName())
                        .lastName(googleInfoDTO.getLastName())
                        .avatar(googleInfoDTO.getPicture())
                        .password("")
                        .role(defaultRole)
                        .isBlock(false)
                        .status("ACTIVE")
                        .createdDate(LocalDateTime.now())
                        .updatedDate(LocalDateTime.now())
                        .build();
                user = userRepository.save(user);
                if (user != null) {
                    //                   Gửi sự kiện cho codinviec core tạo user
                    authEventPublisher.publishUserRegisteredSuccess(CreateUserCorePayload.builder()
                            .id(user.getId())
                            .avatar(user.getAvatar())
                            .email(user.getEmail())
                            .password(user.getPassword())
                            .firstName(user.getFirstName())
                            .lastName(user.getLastName())
                            .isBlock(user.getIsBlock())
                            .status(user.getStatus())
                            .createdDate(user.getCreatedDate())
                            .updatedDate(user.getUpdatedDate())
                            .roleName(user.getRole().getRoleName())
                            .build());
                } else {
                    throw new LoginFaildExceptionHandler("Login google thất bại!");
                }
            } else {
                // Nếu user đã tồn tại, cập nhật thông tin từ Google
                if (googleInfoDTO.getFirstName() == null || googleInfoDTO.getFirstName().isEmpty()) {
                    user.setFirstName(googleInfoDTO.getFirstName());
                }

                if (googleInfoDTO.getLastName() == null || googleInfoDTO.getLastName().isEmpty()) {
                    user.setLastName(googleInfoDTO.getLastName());
                }

                if (googleInfoDTO.getPicture() == null || googleInfoDTO.getPicture().isEmpty()) {
                    user.setAvatar(googleInfoDTO.getPicture());
                }

                user.setUpdatedDate(LocalDateTime.now());
                user = userRepository.save(user);
            }

            //        xử lí tokenVersion
            int tokenVersion = 1;
            if (redisTemplateDb.hasKey(keyVersionRedis + user.getId())) {
                tokenVersion = Integer.parseInt(Objects.requireNonNull(redisTemplateDb.opsForValue().get(keyVersionRedis + user.getId())));
            } else {
                redisTemplateDb.opsForValue().set(keyVersionRedis + user.getId(), String.valueOf(tokenVersion));
            }
            String devicedID = UUID.randomUUID().toString();

            String accessToken = jwtHealper.createAccessToken(user.getRole().getRoleName(), tokenVersion, user.getId(), devicedID);
            String refershToken = jwtHealper.createRefreshToken(user.getRole().getRoleName(), user.getId(), keyRefreshTokenRedis, tokenVersion, devicedID);
            deviceSessionService.registerDevice(user.getId(), devicedID, keyRefreshTokenRedis);
            return TokenDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refershToken)
                    .build();
        } catch (Exception e) {
            throw new LoginFaildExceptionHandler("Login google thất bại!");
        }


    }

    @Override
    public String buildUrlLoginGoogle() {
        return "https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id=" + clientId
                + "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
                + "&response_type=code"
                + "&scope=openid%20email%20profile"
                + "&access_type=offline"
                + "&prompt=consent";
    }

}
