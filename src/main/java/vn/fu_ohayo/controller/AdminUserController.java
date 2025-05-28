package vn.fu_ohayo.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.SearchUserRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.UserResponse;
import vn.fu_ohayo.service.UserService;

import java.util.List;

@RestController()
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/admin/users")
public class AdminUserController {

    UserService userService;

    @GetMapping
    public ApiResponse<List<UserResponse>> searchUsers(@Valid @ModelAttribute SearchUserRequest request) {
        return ApiResponse.<List<UserResponse>>builder()
                .code("200")
                .status("success")
                .message("Get user by name successfully")
                .data(userService.searchUsersByName(request))
                .build();
    }
}
