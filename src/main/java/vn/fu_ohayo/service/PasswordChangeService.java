package vn.fu_ohayo.service;

import org.springframework.stereotype.Service;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.repository.UserRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class PasswordChangeService {

    private final UserRepository userRepository;

    public PasswordChangeService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean changePassword(User user, String currentPassword, String newPassword, String confirmPassword) {
        String currentHashed = hashPassword(currentPassword);
        if (!currentHashed.equals(user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("New passwords do not match.");
        }
        if (newPassword.length() < 8) {
            throw new IllegalArgumentException("New password must be at least 8 characters.");
        }
        String newHashed = hashPassword(newPassword);
        user.setPassword(newHashed);
        userRepository.save(user);
        return true;
    }

    private String hashPassword(String plainPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(plainPassword.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}