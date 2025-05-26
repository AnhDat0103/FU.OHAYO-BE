package vn.fu_ohayo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.service.PasswordForgotService;
import vn.fu_ohayo.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final PasswordForgotService passwordForgotService;
    private final UserRepository userRepository;

    public AuthController(PasswordForgotService passwordForgotService, UserRepository userRepository) {
        this.passwordForgotService = passwordForgotService;
        this.userRepository = userRepository;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        if (!userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(404).body("Email not found");
        }
        passwordForgotService.createAndSendToken(request.getEmail());
        return ResponseEntity.ok().body("Reset link sent if email exists");
    }

    public static class ForgotPasswordRequest {
        private String email;
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
}