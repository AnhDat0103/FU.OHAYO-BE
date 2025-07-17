package vn.fu_ohayo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.fu_ohayo.config.AuthConfig;
import vn.fu_ohayo.dto.request.*;
import vn.fu_ohayo.dto.request.Admin.User.AdminCreateUserRequest;
import vn.fu_ohayo.dto.request.Admin.User.AdminFilterUserRequest;
import vn.fu_ohayo.dto.request.Admin.User.AdminUpdateUserRequest;
import vn.fu_ohayo.dto.response.Admin.User.AdminCheckEmailUserResponse;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.Admin.User.AdminFilterUserResponse;
import vn.fu_ohayo.dto.response.UserResponse;
import vn.fu_ohayo.entity.*;
import vn.fu_ohayo.enums.*;
import vn.fu_ohayo.enums.MembershipLevel;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.UserMapper;
import vn.fu_ohayo.repository.*;
import vn.fu_ohayo.service.JwtService;
import vn.fu_ohayo.service.MailService;
import vn.fu_ohayo.service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Transactional
@Slf4j(topic = "UserService")
public class UserServiceImp implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    AuthConfig configuration;
    MailService mailService;
    JwtService jwtService;
    RoleRepository roleRepository;
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
    @Transactional(readOnly = true)
    public Page<AdminFilterUserResponse> filterUsersForAdmin(AdminFilterUserRequest request) {
        return userRepository.filterUsers(
                request.getFullName(),
                request.getMembershipLevel(),
                request.getStatus(),
                request.getRegisteredFrom(),
                request.getRegisteredTo(),
                PageRequest.of(request.getCurrentPage(), request.getPageSize())
        ).map(userMapper::toAdminFilterUserResponse);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        user.setDeleted(true);
        userRepository.save(user);
    }

    @Override
    public UserResponse updateUser(Long userId, AdminUpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorEnum.EMAIL_EXIST);
        }

        userMapper.updateUserFromAdminRequest(user, request);

        return userMapper.toUserResponse(userRepository.save(user));
    }


    @Override
    public UserResponse createUser(AdminCreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorEnum.EMAIL_EXIST);
        }

        if (userRepository.existsByPhone(request.getPhone())) {
            throw new AppException(ErrorEnum.PHONE_EXIST);
        }

        return userMapper.toUserResponse(userRepository.save(userMapper.toUser(request)));
    }

    @Override
    public void recoverUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        user.setDeleted(false);
        user.setUpdatedAt(new Date());
        userRepository.save(user);
    }

    @Override
    public AdminCheckEmailUserResponse checkEmailExists(String email) {
        Optional<User> optionalUser = userRepository.findByEmailIncludingDeleted(email);

        if (optionalUser.isPresent()) {
            return userMapper.toAdminCheckEmailUserResponse(optionalUser.get());
        }

        return new AdminCheckEmailUserResponse();
    }

    @Override
    public UserResponse registerUser(UserRegister userRegister) {
        return null;
    }


    @Override
    public ApiResponse<?> registerInitial(InitialRegisterRequest initialRegisterRequest) {

        if(userRepository.existsByEmailAndStatus(initialRegisterRequest.getEmail(), UserStatus.ACTIVE) || userRepository.existsByEmailAndStatus(initialRegisterRequest.getEmail(), UserStatus.BANNED)) {
            throw new AppException(ErrorEnum.EMAIL_EXIST);
        }
        String emailParent = SecurityContextHolder.getContext().getAuthentication().getName();
        if (parentStudentRepository.findByParentEmail(emailParent) != null && parentStudentRepository.findByParentEmail(emailParent).size() == 3) {
            throw new AppException(ErrorEnum.MAX_STUDENT_LIMIT);
        }
        var password = configuration.passwordEncoder().encode(initialRegisterRequest.getPassword());
            User user = null;
        if(!userRepository.existsByEmail(initialRegisterRequest.getEmail())) {
             user = userRepository.save(User.builder().email(initialRegisterRequest.getEmail()).status(UserStatus.INACTIVE).membershipLevel(MembershipLevel.NORMAL).role(roleRepository.findByName(RoleEnum.USER)).provider(Provider.LOCAL).password(password).build());
        }
        else {
            user = userRepository.findByEmail(initialRegisterRequest.getEmail()).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        }
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
        log.info(email);
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

            UserResponse userResponse = userMapper.toUserResponse(user);
            List<ParentStudent> filteredChildren = user.getChildren().stream().filter(parentStudent -> parentStudent.getStudent() != null && parentStudent.getParentCodeStatus() == ParentCodeStatus.CONFIRM).collect(Collectors.toList());
            List<ParentStudent> filterParent = user.getParents().stream().filter(parentStudent -> parentStudent.getParentCodeStatus() == ParentCodeStatus.CONFIRM).collect(Collectors.toList());
            if ("USER".equalsIgnoreCase(userResponse.getRoleName())) {
                List<ParentStudentDTO> list = new ArrayList<>();
                filterParent.forEach(parentStudent -> {list.add(ParentStudentDTO.builder().id(parentStudent.getId()).user(SimpleUserDTO.builder()
                        .userId(parentStudent.getParent().getUserId())
                        .fullName(parentStudent.getParent().getFullName()).build()).build());});
                userResponse.setParents(list);
            } else if ("PARENT".equalsIgnoreCase(userResponse.getRoleName())) {
                List<StudentDTO> list = new ArrayList<>();
                filteredChildren.forEach(parentStudent -> {list.add(StudentDTO.builder().id(parentStudent.getId()).user(SimpleUserDTO.builder()
                        .userId(parentStudent.getStudent().getUserId())
                        .fullName(parentStudent.getStudent().getFullName())
                        .gender(parentStudent.getStudent().getGender()).membershipLevel(parentStudent.getStudent().getMembershipLevel())
                        .build()).build());});
                userResponse.setChildren(list);
            }
            return userResponse;

    }

    @Scheduled(cron = "0 0 0 * * *")
    public void setMemberShipLevel() {
        List<MembershipLevelOfUser> memberships = memberShipLevelOfUserRepository.findAll();
        memberships.forEach((user) -> {
            LocalDate today = LocalDate.now();
            if (user.getEndDate().isBefore(today)) {
                User user1 = user.getUser();
                user1.setMembershipLevel(MembershipLevel.NORMAL);
                userRepository.save(user1);
            }
        });
    }
}
