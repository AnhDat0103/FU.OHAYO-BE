package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.request.SearchUserRequest;
import vn.fu_ohayo.dto.request.UserRegister;
import vn.fu_ohayo.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
    List<UserResponse> searchUsersByName(SearchUserRequest request);
    UserResponse registerUser(UserRegister userRegister);
}
