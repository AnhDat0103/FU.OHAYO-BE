package vn.fu_ohayo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.entity.UserProfileDTO;
import vn.fu_ohayo.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/http://localhost:5173/profile")
public class UserProfileController {

    private UserService userService;

    @GetMapping
    public UserProfileDTO getUserProfile(Principal principal) {
        return userService.getUserProfileDTO(principal.getName());
    }
}

