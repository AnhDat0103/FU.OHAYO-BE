package vn.fu_ohayo.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.UserRegister;
import vn.fu_ohayo.dto.response.UserResponse;
import vn.fu_ohayo.service.UserService;

import java.util.List;

@RestController()
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public UserResponse registerUser(@Valid @RequestBody UserRegister userRegister) {
        return userService.registerUser(userRegister);
    }
}
