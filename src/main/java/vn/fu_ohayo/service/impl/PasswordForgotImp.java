package vn.fu_ohayo.service.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.atn.LexerTypeAction;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.Validate.PasswordResetValidate;
import vn.fu_ohayo.service.PasswordForgotService;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Getter
public class PasswordForgotImp implements PasswordForgotService {
    private final Map<String, TokenInfo> tokenStore = new HashMap<>();
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private Random random = new Random();

    @AllArgsConstructor
    public static class TokenInfo {
        public String email;
        public LocalDateTime expiryTime;
    }

    @Override
    // tao va gui ma xac nhan den email cua nguoi dung
    public void createAndSendToken(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            System.out.println("Email not found.");
            return;
        }
        String token = generateToken(email);
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(15);
        tokenStore.put(token, new TokenInfo(email, expiryTime));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your Password Reset Code");
        message.setText("Your password reset code is: " + token);
        message.setFrom("thai110504@gmail.com");
        mailSender.send(message);
    }

    //    @Override
    public void userInputPassword() {

    }
    @Override
    // doi mat khau cho nguoi dung khi nhap token
    public boolean resetPassword(String token, String newPassword) {
        TokenInfo tokenInfo = tokenStore.get(token);
        // kiem tra token co hop le va chua het han hay khong
        if (tokenInfo == null || PasswordResetValidate.isTokenValid(token, tokenStore)) {
            System.out.println("Invalid or expired token.");
            return false;
        }
        // neu hop le thi cho phep nguoi dung nhap mat khau moi
        if (!PasswordResetValidate.isPasswordNotEmpty(newPassword)) {
            System.out.println("New password cannot be empty.");
            return false;
        }
        if (!PasswordResetValidate.isPasswordLengthValid(newPassword, 8)) {
            System.out.println("New password must be at least 8 characters long.");
            return false;
        }
        Optional<User> userOpt = userRepository.findByEmail(tokenInfo.email);
        if (userOpt.isEmpty())
            return false;
        User user = userOpt.get();
        String newHashed = passwordEncoder.encode(newPassword);
        user.setPassword(newHashed);
        userRepository.save(user);
        tokenStore.remove(token);
        System.out.println("Password reset successfully for " + tokenInfo.email);
        return true;
    }

    private static User getUser(String newPassword, Optional<User> userOpt) {
        User user = userOpt.get();
//        String newHashed = hashPassword(newPassword);
//        if (!PasswordResetValidate.isNewPasswordDifferent(newHashed, user.getPassword())) {
//            System.out.println("New password cannot be the same as the old password.");
//            return false;
//        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String newHashed = passwordEncoder.encode(newPassword);
        //luu mat khau moi vao db va xoa token
        user.setPassword(newHashed);
        return user;
    }

//    @Override
//    public String hashPassword(String plainPassword) {
//        try {
//            MessageDigest md = MessageDigest.getInstance("SHA-256");
//            byte[] hashedBytes = md.digest(plainPassword.getBytes());
//            return Base64.getEncoder().encodeToString(hashedBytes);
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("Error hashing password", e);
//        }
//    }

    @Override
    // tao ma xac nhan ngau nhien 6 so de gui den email
    public String generateToken(String email) {
        Random random = new Random();
        int number = random.nextInt(900000) + 100000;
        return String.valueOf(number);
    }
}

