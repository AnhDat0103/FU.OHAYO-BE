package vn.fu_ohayo.service;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.Validate.PasswordResetValidate;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordChangeService {
    public boolean ChangePassword(String email, String newPassword, String confirmPassword, UserRepository userRepository) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            System.out.println("User not found.");
            return false;
        }
        if (!PasswordResetValidate.isPasswordConfirmed(newPassword, confirmPassword)) {
            System.out.println("Passwords do not match.");
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
        String newHashed = hashPassword(newPassword);
        if (!PasswordResetValidate.isNewPasswordDifferent(newHashed, user.getPassword())) {
            System.out.println("New password cannot be the same as the old password.");
            return false;
        }
        user.setPassword(newHashed);
        userRepository.save(user);
        System.out.println("Password changed successfully.");
        return true;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
