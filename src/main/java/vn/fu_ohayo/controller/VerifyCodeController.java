package vn.fu_ohayo.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.Validate.PasswordResetValidate;
import vn.fu_ohayo.service.impl.PasswordForgotImp;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class VerifyCodeController {

    private final PasswordForgotImp PasswordForgotImp;

    //kiem tra ma xac nhan co dung voi ma da gui den email hay khong
    @PostMapping("/verify-reset-code")
    public ResponseEntity<?> verifyResetCode(@RequestBody VerifyCodeRequest request) {
        System.out.println("Verifying code: " + request.getCode() + " for email: " + request.getEmail());
        PasswordForgotImp.TokenInfo tokenInfo = PasswordForgotImp.getTokenStore().get(request.getCode());
        if (PasswordResetValidate.isTokenValid(request.getCode(), PasswordForgotImp.getTokenStore())) {
            return ResponseEntity.status(404).body("Invalid code");
        }
        if (!tokenInfo.email.equals(request.getEmail())) {
            return ResponseEntity.status(404).body("Code does not match email");
        }
        if (tokenInfo.expiryTime.isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(404).body("Code expired");
        }
        return ResponseEntity.ok("Code verified");
    }

    @Setter
    @Getter
    public static class VerifyCodeRequest {
        private String email;
        private String code;

    }
}