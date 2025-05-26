package vn.fu_ohayo.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.Validate.PasswordResetValidate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PasswordForgotService {
    public final Map<String, TokenInfo> tokenStore = new HashMap<>();
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    public PasswordForgotService(UserRepository userRepository, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    public static class TokenInfo {
        String email;
        public LocalDateTime expiryTime;

        public TokenInfo(String email, LocalDateTime expiryTime) {
            this.email = email;
            this.expiryTime = expiryTime;
        }
    }

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
        message.setFrom("no-reply@fu-ohayo.vn");
        mailSender.send(message);

        System.out.printf("A password reset token has been sent to %s: %s%n", email, token);
    }

    public void userInputPassword() {
        Scanner scanner = new Scanner(System.in);
        String token;
        int attempts = 0;
        final int MAX_ATTEMPTS = 6;
        while (true) {
            if (PasswordResetValidate.isAttemptLimitExceeded(attempts, MAX_ATTEMPTS)) {
                System.out.println("Limit is 6");
                scanner.close();
                return;
            }
            System.out.print("Enter the token you received:");
            token = scanner.nextLine();
            if (PasswordResetValidate.isTokenValid(token, tokenStore)) {
                System.out.println("Invalid or expired token. Please try again.");
                attempts++;
            } else {
                break;
            }
        }
        while (true) {
            System.out.print("Enter your new password:");
            String newPassword = scanner.nextLine();
            System.out.print("Confirm your new password:");
            String confirmPassword = scanner.nextLine();
            if (!PasswordResetValidate.isPasswordConfirmed(newPassword, confirmPassword)) {
                System.out.println("Passwords do not match. Please try again.");
                continue;
            }
            boolean result = resetPassword(token, newPassword);
            if (result) {
                System.out.println("Password reset successful.");
                break;
            } else {
                System.out.println("Password reset failed. Please try again.");
            }
        }
        scanner.close();
    }

    public boolean resetPassword(String token, String newPassword) {
        TokenInfo tokenInfo = tokenStore.get(token);
        if (tokenInfo == null || PasswordResetValidate.isTokenValid(token, tokenStore)) {
            System.out.println("Invalid or expired token.");
            return false;
        }
        if (!PasswordResetValidate.isPasswordNotEmpty(newPassword)) {
            System.out.println("New password cannot be empty.");
            return false;
        }
        if (!PasswordResetValidate.isPasswordLengthValid(newPassword, 8)) {
            System.out.println("New password must be at least 8 characters long.");
            return false;
        }
        Optional<User> userOpt = userRepository.findByEmail(tokenInfo.email);
        if (userOpt.isEmpty()) return false;
        User user = userOpt.get();
        String newHashed = hashPassword(newPassword);
        if (!PasswordResetValidate.isNewPasswordDifferent(newHashed, user.getPassword())) {
            System.out.println("New password cannot be the same as the old password.");
            return false;
        }
        user.setPassword(newHashed);
        userRepository.save(user);
        tokenStore.remove(token);
        System.out.println("Password reset successfully for " + tokenInfo.email);
        return true;
    }

    public String hashPassword(String plainPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(plainPassword.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public String generateToken(String email) {
        Random random = new Random();
        int number = random.nextInt(900000) + 100000;
        return String.valueOf(number);
    }
}