package vn.fu_ohayo.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.AddUserRequest;
import vn.fu_ohayo.dto.request.AdminUpdateUserRequest;
import vn.fu_ohayo.dto.request.SearchUserRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.SearchUserResponse;
import vn.fu_ohayo.dto.response.UserResponse;
import vn.fu_ohayo.service.UserService;

import java.util.List;

import static vn.fu_ohayo.constant.ConstantGolbal.HTTP_SUCCESS_CODE_RESPONSE;
import static vn.fu_ohayo.constant.ConstantGolbal.HTTP_SUCCESS_RESPONSE;

@RestController()
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/admin/users")
public class AdminUserController {
    UserService userService;

    @GetMapping
    public ApiResponse<List<SearchUserResponse>> searchUsers(@Valid @ModelAttribute SearchUserRequest request) {
        return ApiResponse.<List<SearchUserResponse>>builder()
                .code(HTTP_SUCCESS_CODE_RESPONSE)
                .status(HTTP_SUCCESS_RESPONSE)
                .message("Get user by name successfully")
                .data(userService.searchUsersByName(request))
                .build();
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<UserResponse> deleteUser(@PathVariable("userId") Long userId) {
        return ApiResponse.<UserResponse>builder()
                .code("200")
                .status("success")
                .message("Delete user successfully")
                .data(userService.deleteUser(userId))
                .build();
    }

    @PatchMapping("/{userId}")
    public ApiResponse<UserResponse> updateUser(@PathVariable("userId") Long userId,
                                                @RequestBody @Valid AdminUpdateUserRequest adminUpdateUserRequest) {
        return ApiResponse.<UserResponse>builder()
                .code("200")
                .status("success")
                .message("Update user successfully")
                .data(userService.updateUser(userId, adminUpdateUserRequest))
                .build();
    }

    @PostMapping
    public ApiResponse<UserResponse> addUser(@RequestBody @Valid AddUserRequest addUserRequest) {
        return ApiResponse.<UserResponse>builder()
                .code("200")
                .status("success")
                .message("Create user successfully")
                .data(userService.addUser(addUserRequest))
                .build();
    }
}
