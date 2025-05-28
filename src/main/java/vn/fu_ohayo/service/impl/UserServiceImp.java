package vn.fu_ohayo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.config.AuthConfig;
import vn.fu_ohayo.dto.request.CompleteProfileRequest;
import vn.fu_ohayo.dto.request.InitialRegisterRequest;
import vn.fu_ohayo.dto.request.SearchUserRequest;
import vn.fu_ohayo.dto.request.UserRegister;
import vn.fu_ohayo.dto.response.UserResponse;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.MembershipLevel;
import vn.fu_ohayo.enums.Provider;
import vn.fu_ohayo.mapper.UserMapper;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.JwtService;
import vn.fu_ohayo.service.MailService;
import vn.fu_ohayo.service.UserService;

import java.util.List;
@RequiredArgsConstructor
@Service
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)

public class UserServiceImp implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    AuthConfig configuration;
    MailService mailService;
    JwtService jwtService;




    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }


    @Override
    public List<UserResponse> searchUsersByName(SearchUserRequest request) {
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
                .map(userMapper::toUserResponse)
                .toList();
    }

    @Override
    public UserResponse registerUser(UserRegister userRegister) {
        return null;
    }


    @Override
    public void registerInitial(InitialRegisterRequest initialRegisterRequest) {

        if (userRepository.existsByEmail(initialRegisterRequest.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        var password = configuration.passwordEncoder().encode(initialRegisterRequest.getPassword());
        var user = userRepository.save(User.builder().email(initialRegisterRequest.getEmail()).membershipLevel(MembershipLevel.NORMAL).provider(Provider.LOCAL).password(password).build());
        String token = jwtService.generateAccessToken(user.getUserId(), initialRegisterRequest.getEmail(), null);
        mailService.sendEmail(initialRegisterRequest.getEmail(),token);
    }



    @Override
    public UserResponse completeProfile(CompleteProfileRequest completeProfileRequest, String email){
        var user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        user.setFullName(completeProfileRequest.getFullName());
        user.setGender(completeProfileRequest.getGender());
        user.setAddress(completeProfileRequest.getAddress());
        user.setPhone(completeProfileRequest.getPhone());
        user.setDob(completeProfileRequest.getDob());

        userRepository.save(user);
        return userMapper.toUserResponse(user);



    }


}
