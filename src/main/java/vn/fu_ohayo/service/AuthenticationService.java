package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.request.AdminLoginRequest;
import vn.fu_ohayo.dto.request.SignInRequest;
import vn.fu_ohayo.dto.response.TokenResponse;
import vn.fu_ohayo.enums.Provider;

public interface AuthenticationService {
    TokenResponse getAccessToken(SignInRequest request);
    TokenResponse getRefreshToken(String request, String typeLogin);
    TokenResponse getAccessTokenForSocialLogin(String email, Provider provider);
    TokenResponse getAccessTokenForAdmin(AdminLoginRequest request);
}
