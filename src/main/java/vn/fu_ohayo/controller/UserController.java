package vn.fu_ohayo.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.SignInRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.TokenResponse;
import vn.fu_ohayo.dto.response.UserResponse;
import vn.fu_ohayo.service.AuthenticationService;
import vn.fu_ohayo.service.UserService;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController()
@RequestMapping("/users")

public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    public UserController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .code("200")
                .status("OK")
                .message("Get all users successfully")
                .data(userService.getAllUsers())
                .build();
    }

//    @PostMapping
//    public UserResponse registerUser(@Valid @RequestBody UserRegister userRegister) {
//        return userService.registerUser(userRegister);
//    }

    @PostMapping
    public TokenResponse signInUser(@Valid @RequestBody SignInRequest sign) {
        return authenticationService.getAccessToken(sign);
    }

}
