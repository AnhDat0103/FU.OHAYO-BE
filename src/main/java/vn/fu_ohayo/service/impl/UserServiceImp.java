package vn.fu_ohayo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.config.AuthConfig;
import vn.fu_ohayo.dto.request.CompleteProfileRequest;
import vn.fu_ohayo.dto.request.InitialRegisterRequest;
import vn.fu_ohayo.dto.request.AddUserRequest;
import vn.fu_ohayo.dto.request.AdminUpdateUserRequest;
import vn.fu_ohayo.dto.request.SearchUserRequest;
import vn.fu_ohayo.dto.request.UserRegister;
import vn.fu_ohayo.dto.response.SearchUserResponse;
import vn.fu_ohayo.dto.response.UserResponse;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.entity.UserProfileDTO;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.MembershipLevel;
import vn.fu_ohayo.enums.Provider;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.enums.UserStatus;
import vn.fu_ohayo.mapper.AdminUpdateUserMapper;
import vn.fu_ohayo.mapper.SearchUserMapper;
import vn.fu_ohayo.mapper.UserMapper;
import vn.fu_ohayo.repository.RoleRepository;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.JwtService;
import vn.fu_ohayo.service.MailService;
import vn.fu_ohayo.service.UserService;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Service
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Slf4j(topic = "UserService")
public class UserServiceImp implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    AuthConfig configuration;
    MailService mailService;
    JwtService jwtService;
    AdminUpdateUserMapper adminUpdateUserMapper;
    RoleRepository roleRepository;
    private final SearchUserMapper searchUserMapper;


    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    @Override
    public List<SearchUserResponse> searchUsersByName(SearchUserRequest request) {
        return userRepository.findAll()
                .stream()
                .filter(user -> request.getFullName() == null
                        || user.getFullName().toLowerCase().contains(request.getFullName().toLowerCase()))
                .filter(user -> request.getMembershipLevel() == null
                        || user.getMembershipLevel() == request.getMembershipLevel())
                .filter(user -> request.getStatus() == null
                        || user.getStatus() == request.getStatus())
                .filter(user -> request.getRegisteredFrom() == null
                        || !user.getCreatedAt().before(request.getRegisteredFrom()))
                .filter(user -> request.getRegisteredTo() == null
                        || !user.getCreatedAt().after(request.getRegisteredTo()))
                .map(searchUserMapper::toSearchUserResponse)
                .toList();
    }

    @Override
    public UserResponse registerUser(UserRegister userRegister) {
        return null;
    }


    @Override
    public boolean registerInitial(InitialRegisterRequest initialRegisterRequest) {

        if (userRepository.existsByEmailAndProvider(initialRegisterRequest.getEmail(), Provider.LOCAL)) {
            return false;
        }
        var password = configuration.passwordEncoder().encode(initialRegisterRequest.getPassword());
        var user = userRepository.save(User.builder().email(initialRegisterRequest.getEmail()).status(UserStatus.INACTIVE).membershipLevel(MembershipLevel.NORMAL).provider(Provider.LOCAL).password(password).build());
        String token = jwtService.generateAccessToken(user.getUserId(), initialRegisterRequest.getEmail(), null);
        mailService.sendEmail(initialRegisterRequest.getEmail(),token);
        return true;
    }



    @Override
    public UserResponse completeProfile(CompleteProfileRequest completeProfileRequest, String email){
        User user = userRepository.findByEmail(email).orElseThrow(()-> new AppException(ErrorEnum.USER_NOT_FOUND));

        log.info(String.valueOf(completeProfileRequest.getRole()));

        user.setStatus(UserStatus.ACTIVE);
        user.setFullName(completeProfileRequest.getFullName());
        user.setGender(completeProfileRequest.getGender());
        user.setAddress(completeProfileRequest.getAddress());
        user.setPhone(completeProfileRequest.getPhone());
        user.setDob(completeProfileRequest.getDob());
        user.setRole(roleRepository.findByName(completeProfileRequest.getRole()));

        userRepository.save(user);
        return userMapper.toUserResponse(user);



    }

    public UserProfileDTO getUserProfileDTO(String fullname) {
        User user = userRepository.findByFullName(fullname).orElseThrow(()
                -> new RuntimeException("User not found"));
        UserProfileDTO dto = new UserProfileDTO();
        dto.setFullName(fullname);
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setPhoneNumber(user.getPhone());
        dto.setAddress(user.getAddress());
        dto.setStatus(user.getStatus());
        dto.setProvider(user.getProvider());
//        dto.setProfilePicture(user.getAvatar());
        if (user.getCreatedAt() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            dto.setMemberSince("Member since " + user.getCreatedAt().toInstant()
                    .atZone(java.time.ZoneId.systemDefault()).toLocalDate()
                    .format(formatter));
        }
        dto.setHasSettingsAccsees(true);
        return dto;
    }


    @Override
    public UserResponse deleteUser(Long userId) {
        UserResponse userResponse = userMapper.toUserResponse(
                userRepository.findById(userId)
                        .orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND))
        );
        userRepository.deleteById(userId);
        return userResponse;
    }

    @Override
    public UserResponse updateUser(Long userId, AdminUpdateUserRequest adminUpdateUserRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));

        if (adminUpdateUserRequest.getFullName() != null)
            user.setFullName(adminUpdateUserRequest.getFullName());

        if (adminUpdateUserRequest.getEmail() != null)
            user.setEmail(adminUpdateUserRequest.getEmail());

        if (adminUpdateUserRequest.getStatus() != null)
            user.setStatus(UserStatus.valueOf(adminUpdateUserRequest.getStatus()));

        if (adminUpdateUserRequest.getMembershipLevel() != null)
            user.setMembershipLevel(MembershipLevel.valueOf(adminUpdateUserRequest.getMembershipLevel()));

        return adminUpdateUserMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse addUser(AddUserRequest addUserRequest) {
        if (userRepository.existsByEmail(addUserRequest.getEmail())) {
            throw new AppException(ErrorEnum.EMAIL_EXIST);
        }

        if (userRepository.existsByPhone(addUserRequest.getPhone())) {
            throw new AppException(ErrorEnum.PHONE_EXIST);
        }

        return userMapper.toUserResponse(userRepository.save(userMapper.toUser(addUserRequest)));
    }


}
