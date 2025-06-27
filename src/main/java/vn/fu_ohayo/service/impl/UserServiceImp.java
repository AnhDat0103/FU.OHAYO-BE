package vn.fu_ohayo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.config.AuthConfig;
import vn.fu_ohayo.dto.request.CompleteProfileRequest;
import vn.fu_ohayo.dto.request.InitialRegisterRequest;
import vn.fu_ohayo.dto.request.AddUserRequest;
import vn.fu_ohayo.dto.request.AdminUpdateUserRequest;
import vn.fu_ohayo.dto.request.AdminSearchUserRequest;
import vn.fu_ohayo.dto.request.UserRegister;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.AdminSearchUserResponse;
import vn.fu_ohayo.dto.response.UserResponse;
import vn.fu_ohayo.entity.*;
import vn.fu_ohayo.enums.*;
import vn.fu_ohayo.enums.MembershipLevel;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.AdminUpdateUserMapper;
import vn.fu_ohayo.mapper.SearchUserMapper;
import vn.fu_ohayo.mapper.UserMapper;
import vn.fu_ohayo.repository.*;
import vn.fu_ohayo.service.JwtService;
import vn.fu_ohayo.service.MailService;
import vn.fu_ohayo.service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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
    SearchUserMapper searchUserMapper;
    ParentStudentRepository parentStudentRepository;
    MemberShipLevelOfUserRepository memberShipLevelOfUserRepository;


    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    @Override
    public Page<AdminSearchUserResponse> filterUsers(AdminSearchUserRequest request) {
        Page<User> users = userRepository.filterUsers(
                request.getFullName(),
                request.getMembershipLevel(),
                request.getStatus(),
                request.getRegisteredFrom(),
                request.getRegisteredTo(),
                PageRequest.of(request.getCurrentPage(), request.getPageSize())
        );

        return users.map(searchUserMapper::toSearchUserResponse);
    }


    @Override
    public UserResponse registerUser(UserRegister userRegister) {
        return null;
    }


    @Override
    public ApiResponse<?> registerInitial(InitialRegisterRequest initialRegisterRequest) {

        if(!userRepository.existsByEmailAndStatus(initialRegisterRequest.getEmail(), UserStatus.INACTIVE)) {
            throw new AppException(ErrorEnum.EMAIL_EXIST);
        }
        String emailParent = SecurityContextHolder.getContext().getAuthentication().getName();
        if (parentStudentRepository.findByParentEmail(emailParent) != null && parentStudentRepository.findByParentEmail(emailParent).size() == 3) {
            return ApiResponse.builder()
                    .message("You can only create 3 Students")
                    .code("1001")
                    .status("FAIL")
                    .build();
        }
        var password = configuration.passwordEncoder().encode(initialRegisterRequest.getPassword());
        var user = userRepository.save(User.builder().email(initialRegisterRequest.getEmail()).status(UserStatus.INACTIVE).membershipLevel(MembershipLevel.NORMAL).provider(Provider.LOCAL).password(password).build());
        if (!emailParent.equals("anonymousUser")) {
            User parent = userRepository.findByEmail(emailParent).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
            ParentStudent parentStudent = ParentStudent.builder()
                    .verificationCode("")
                    .parent(parent)
                    .student(user)
                    .parentCodeStatus(ParentCodeStatus.CONFIRM)
                    .build();
            parentStudentRepository.save(parentStudent);
        }
        String token = jwtService.generateAccessToken(user.getUserId(), initialRegisterRequest.getEmail(), null);
        mailService.sendEmail(initialRegisterRequest.getEmail(), token);
        return ApiResponse.<InitialRegisterRequest>builder()
                .code("200")
                .status("OK")
                .message("User registered successfully")
                .build();

    }


    @Override
    public UserResponse completeProfile(CompleteProfileRequest completeProfileRequest, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));

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
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        user.setDeleted(true);
        userRepository.save(user);
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

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
    }

    @Override
    public String updateAvatar(String email, String avatarUrl) {
        User user = getUserByEmail(email);
        user.setAvatar(avatarUrl);
        return userRepository.save(user).getAvatar();
    }

    @Override
    public UserResponse getUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        MembershipLevelOfUser membershipLevelOfUser = memberShipLevelOfUserRepository.findByUserUserId(user.getUserId());

        if (membershipLevelOfUser != null && membershipLevelOfUser.getEndDate().isAfter(LocalDate.now())) {
            user.setMembershipLevel(MembershipLevel.ONE_MONTH);
        }
        else if(membershipLevelOfUser != null && !membershipLevelOfUser.getEndDate().isAfter(LocalDate.now())) {
            user.setMembershipLevel(MembershipLevel.NORMAL);
        }
        userRepository.save(user);
            UserResponse userResponse = userMapper.toUserResponse(user);
            List<ParentStudent> filteredChildren = user.getChildren().stream().filter(parentStudent -> parentStudent.getStudent() != null && parentStudent.getParentCodeStatus() == ParentCodeStatus.CONFIRM).collect(Collectors.toList());
            List<ParentStudent> filterParent = user.getParents().stream().filter(parentStudent -> parentStudent.getParentCodeStatus() == ParentCodeStatus.CONFIRM).collect(Collectors.toList());
            filteredChildren.forEach(parentStudent -> {
                MembershipLevelOfUser membershipLevelOfUser1 = memberShipLevelOfUserRepository.findByUserUserId(parentStudent.getStudent().getUserId());
                 if(membershipLevelOfUser1 != null && !membershipLevelOfUser1.getEndDate().isAfter(LocalDate.now())) {
                     User user1 = userRepository.findByEmail(parentStudent.getStudent().getEmail()).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
                     user1.setMembershipLevel(MembershipLevel.NORMAL);
                     userRepository.save(user1);
                }

            });
            if ("USER".equalsIgnoreCase(userResponse.getRoleName())) {
                userResponse.setParents(userMapper.toParentOnlyDtoList(filterParent));

            } else if ("PARENT".equalsIgnoreCase(userResponse.getRoleName())) {
                userResponse.setChildren(userMapper.toStudentOnlyDtoList(filteredChildren));

            }        return userResponse;

    }

}
