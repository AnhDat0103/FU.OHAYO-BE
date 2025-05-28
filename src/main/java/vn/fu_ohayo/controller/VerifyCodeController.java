//package vn.fu_ohayo.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import vn.fu_ohayo.Validate.PasswordResetValidate;
//import vn.fu_ohayo.service.PasswordForgotService;
//
//@RestController
//@RequestMapping("/api/auth")
//@RequiredArgsConstructor
//public class VerifyCodeController {
//
//    private final PasswordForgotService passwordForgotService;
//
//    @PostMapping("/verify-reset-code")
//    public ResponseEntity<?> verifyResetCode(@RequestBody VerifyCodeRequest request) {
//        System.out.println("Verifying code: " + request.getCode() + " for email: " + request.getEmail());
//        PasswordForgotService.TokenInfo tokenInfo = passwordForgotService.tokenStore.get(request.getCode());
//        if (PasswordResetValidate.isTokenValid(request.getCode(), passwordForgotService.tokenStore)) {
//            return ResponseEntity.status(400).body("Invalid or expired code");
//        }
//        if (!tokenInfo.email.equals(request.getEmail())) {
//            return ResponseEntity.status(400).body("Code does not match email");
//        }
//        if (tokenInfo.expiryTime.isBefore(java.time.LocalDateTime.now())) {
//            return ResponseEntity.status(400).body("Code expired");
//        }
//        return ResponseEntity.ok("Code verified");
//    }
//
//    public static class VerifyCodeRequest {
//        private String email;
//        private String code;
//        public String getEmail() { return email; }
//        public void setEmail(String email) { this.email = email; }
//        public String getCode() { return code; }
//        public void setCode(String code) { this.code = code; }
//    }
//}