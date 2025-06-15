package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.request.AddUserRequest;
import vn.fu_ohayo.dto.request.AdminUpdateUserRequest;
import vn.fu_ohayo.dto.request.CompleteProfileRequest;
import vn.fu_ohayo.dto.request.InitialRegisterRequest;
import vn.fu_ohayo.dto.request.SearchUserRequest;
import vn.fu_ohayo.dto.request.UserRegister;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.SearchUserResponse;
import vn.fu_ohayo.dto.response.UserResponse;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.entity.UserProfileDTO;

import java.util.List;

public interface UserService {
    UserProfileDTO getUserProfileDTO(String email);
    List<UserResponse> getAllUsers();

    ApiResponse<?> registerInitial(InitialRegisterRequest initialRegisterRequest);

    UserResponse completeProfile(CompleteProfileRequest completeProfileRequest, String email);

    List<SearchUserResponse> searchUsersByName(SearchUserRequest request);

    UserResponse registerUser(UserRegister userRegister);
    UserResponse deleteUser(Long userId);
    UserResponse updateUser(Long userId, AdminUpdateUserRequest adminUpdateUserRequest);
    UserResponse addUser(AddUserRequest addUserRequest);
    User getUserByEmail(String email);
    User getUserById(Long userId);
    String updateAvatar(String email, String avatarUrl);
    UserResponse getUser();
}