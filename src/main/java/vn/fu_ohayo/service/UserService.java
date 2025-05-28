package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.request.CompleteProfileRequest;
import vn.fu_ohayo.dto.request.InitialRegisterRequest;
import vn.fu_ohayo.dto.request.SignInRequest;
import vn.fu_ohayo.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
    void registerInitial(InitialRegisterRequest initialRegisterRequest);
    UserResponse completeProfile(CompleteProfileRequest completeProfileRequest , String email);
}
