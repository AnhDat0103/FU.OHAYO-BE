package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.request.SignInRequest;
import vn.fu_ohayo.dto.response.TokenResponse;

public interface AuthenticationService {
    TokenResponse getAccessToken(SignInRequest request);
    TokenResponse getRefreshToken(String request);

}
