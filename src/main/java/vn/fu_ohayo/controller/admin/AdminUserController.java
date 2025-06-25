package vn.fu_ohayo.controller.admin;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.AddUserRequest;
import vn.fu_ohayo.dto.request.AdminUpdateUserRequest;
import vn.fu_ohayo.dto.request.AdminSearchUserRequest;
import vn.fu_ohayo.dto.response.AdminSearchUserResponse;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.UserResponse;
import vn.fu_ohayo.service.UserService;

import static vn.fu_ohayo.constant.ConstantGolbal.HTTP_SUCCESS_CODE_RESPONSE;
import static vn.fu_ohayo.constant.ConstantGolbal.HTTP_SUCCESS_RESPONSE;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserController {
    private final UserService userService;

    @GetMapping
    public ApiResponse<Page<AdminSearchUserResponse>> filterUsers(@Valid @ModelAttribute AdminSearchUserRequest request) {
        return ApiResponse.<Page<AdminSearchUserResponse>>builder()
                .code(HTTP_SUCCESS_CODE_RESPONSE)
                .status(HTTP_SUCCESS_RESPONSE)
                .message("Get user by name successfully")
                .data(userService.filterUsers(request))
                .build();
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<Void> deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return ApiResponse.<Void>builder()
                .code("204")
                .status("success no return")
                .message("Delete user successfully")
                .build();
    }

    @PatchMapping("/{userId}")
    public ApiResponse<UserResponse> updateUser(@PathVariable("userId") Long userId,
                                                @RequestBody @Valid AdminUpdateUserRequest adminUpdateUserRequest) {
        return ApiResponse.<UserResponse>builder()
                .code(HTTP_SUCCESS_CODE_RESPONSE)
                .status(HTTP_SUCCESS_RESPONSE)
                .message("Update user successfully")
                .data(userService.updateUser(userId, adminUpdateUserRequest))
                .build();
    }

    @PostMapping
    public ApiResponse<UserResponse> addUser(@RequestBody @Valid AddUserRequest addUserRequest) {
        return ApiResponse.<UserResponse>builder()
                .code("201")
                .status("success")
                .message("Create user successfully")
                .data(userService.addUser(addUserRequest))
                .build();
    }
}
