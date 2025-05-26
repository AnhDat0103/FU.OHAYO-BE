package vn.fu_ohayo.Validate;

import vn.fu_ohayo.service.PasswordForgotService.TokenInfo;

import java.time.LocalDateTime;
import java.util.Map;

public class PasswordResetValidate {

    public static boolean isTokenValid(String token, Map<String, TokenInfo> tokenStore) {
        TokenInfo tokenInfo = tokenStore.get(token);
        return tokenInfo != null && tokenInfo.expiryTime.isAfter(LocalDateTime.now());
    }

    public static boolean isAttemptLimitExceeded(int attempts, int maxAttempts) {
        return attempts >= maxAttempts;
    }

    public static boolean isPasswordConfirmed(String password, String confirmPassword) {
        return password != null && password.equals(confirmPassword);
    }

    public static boolean isPasswordNotEmpty(String password) {
        return password != null && !password.isEmpty();
    }

    public static boolean isPasswordLengthValid(String password, int minLength) {
        return password != null && password.length() >= minLength;
    }

    public static boolean isNewPasswordDifferent(String newHashed, String oldHashed) {
        return !newHashed.equals(oldHashed);
    }
}