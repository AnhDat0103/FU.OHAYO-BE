package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.request.CompleteProfileRequest;
import vn.fu_ohayo.dto.request.InitialRegisterRequest;
import vn.fu_ohayo.dto.request.SignInRequest;
import vn.fu_ohayo.dto.request.SearchUserRequest;
import vn.fu_ohayo.dto.request.UserRegister;
import vn.fu_ohayo.dto.response.UserResponse;
import vn.fu_ohayo.entity.UserProfileDTO;

import java.util.List;

public interface UserService {
    UserProfileDTO getUserProfileDTO(String email);
    List<UserResponse> getAllUsers();

    void registerInitial(InitialRegisterRequest initialRegisterRequest);

    UserResponse completeProfile(CompleteProfileRequest completeProfileRequest, String email);

    List<UserResponse> searchUsersByName(SearchUserRequest request);

    UserResponse registerUser(UserRegister userRegister);
}