package vn.fu_ohayo.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PRService {
    public final Map<String, TokenInfo> tokenStore = new HashMap<>();

    @Autowired
    private UserRepository userRepository;

    static class TokenInfo {
        String email;
        LocalDateTime expiryTime;

        public TokenInfo(String email, LocalDateTime expiryTime) {
            this.email = email;
            this.expiryTime = expiryTime;
        }
    }

    public void createAndSendToken(String email) {
        String token = generateToken(email);
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(15);
        tokenStore.put(token, new TokenInfo(email, expiryTime));
        System.out.println("Token created for " + email + ": " + token);
    }

    public boolean resetPassword(String token, String newPassword) {
        TokenInfo tokenInfo = tokenStore.get(token);
        if (tokenInfo == null || tokenInfo.expiryTime.isBefore(LocalDateTime.now())) {
            System.out.println("Invalid or expired token.");
            return false;
        }
        if (newPassword == null || newPassword.isEmpty()) {
            System.out.println("New password cannot be empty.");
            return false;
        }
        if (newPassword.length() < 8) {
            System.out.println("New password must be at least 8 characters long.");
            return false;
        }
        Optional<User> userOpt = userRepository.findByEmail(tokenInfo.email);
        if (userOpt.isEmpty()) return false;
        User user = userOpt.get();
        user.setPassword(hashPassword(newPassword));
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
        String token = UUID.randomUUID().toString();
        return hashPassword(token + email);
    }
}

