package vn.fu_ohayo.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.service.PasswordForgotService;
import vn.fu_ohayo.repository.UserRepository;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final PasswordForgotService passwordForgotService;
    private final UserRepository userRepository;

//    public AuthController(PasswordForgotService passwordForgotService, UserRepository userRepository) {
//        this.passwordForgotService = passwordForgotService;
//        this.userRepository = userRepository;
//    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        if (!userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(404).body("Email not found");
        }
        passwordForgotService.createAndSendToken(request.getEmail());
        return ResponseEntity.ok("Reset code sent if email exists");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        boolean result = passwordForgotService.resetPassword(request.getToken(), request.getNewPassword());
        if (result) {
            return ResponseEntity.ok("Password reset successful");
        } else {
            return ResponseEntity.badRequest().body("Failed to reset password");
        }
    }
    @Getter
    @Setter
    public static class ForgotPasswordRequest {
        private String email;
    }
    @Getter
    @Setter
    public static class ResetPasswordRequest {
        private String token;
        private String newPassword;
        private String confirmPassword;
    }
}