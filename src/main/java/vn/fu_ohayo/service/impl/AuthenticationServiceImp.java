package vn.fu_ohayo.service.impl;

import com.fasterxml.jackson.core.io.JsonEOFException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.SignInRequest;
import vn.fu_ohayo.dto.response.TokenResponse;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.TokenType;
import vn.fu_ohayo.enums.UserStatus;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.AuthenticationService;
import vn.fu_ohayo.service.JwtService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE)

public class AuthenticationServiceImp implements AuthenticationService{
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    String googleRedirectUri;

//    @Value("${spring.security.oauth2.client.registration.github.client-id}")
//     String githubClientId;
//
//    @Value("${spring.security.oauth2.client.registration.github.redirect-uri}")
//     String githubRedirectUri;

    @Value("${spring.security.oauth2.client.registration.facebook.client-id}")
     String facebookClientId;

    @Value("${spring.security.oauth2.client.registration.facebook.redirect-uri}")
     String facebookRedirectUri;



    final UserRepository userRepository;
    final JwtService jwtService;

    public AuthenticationServiceImp(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    AuthenticationManager authenticationManager;

    @Override
    public TokenResponse getAccessToken(SignInRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException e) {
            throw new AccessDeniedException(e.getMessage());
        }
        var user = userRepository.findByEmail(request.getEmail());
        if (user == null || user.getStatus().equals(UserStatus.ACTIVE)) {
            throw new UsernameNotFoundException("User not found");
        }
        String accessToken = jwtService.generateAccessToken(user.getUserId(), user.getEmail(), null);
        String refreshToken = jwtService.generateRefreshToken(1, request.getEmail(), null);
        return TokenResponse.builder().refreshToken(refreshToken).accessToken(accessToken).build();
    }

    @Override
    public TokenResponse getRefreshToken(String request) {
        return null;
    }

    public boolean extractToken(String token) {
        try {
            var email = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);
            if (email != null && userRepository.existsByEmail(email)) {
                User user =  userRepository.findByEmail(email);
                user.setStatus(UserStatus.ACTIVE);
                userRepository.save(user);
                return true;
            } else {
                throw new UsernameNotFoundException("User not found");
            }
        } catch (AccessDeniedException e) {
            return false;
        }
    }

    public String generateAuthURL(String type) {
        String baseUrl;
        String clientId;
        String redirectUri;
        String scope;
        String responseType = "code";

        switch (type.toLowerCase()) {
            case "google":
                baseUrl = "https://accounts.google.com/o/oauth2/v2/auth";
                clientId = googleClientId;
                redirectUri = "http://localhost:8080/auth/code/google";
                scope = "email profile";
                break;
            case "facebook":
                baseUrl = "https://www.facebook.com/v11.0/dialog/oauth";
                clientId = facebookClientId;
                redirectUri = "http://localhost:8080/auth/code/facebook";
                scope = "email";
                break;
            default:
                throw new IllegalArgumentException("Unsupported OAuth provider: " + type);
        }

        return baseUrl + "?" +
                "client_id=" + clientId +
                "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
                "&response_type=" + responseType +
                "&scope=" + URLEncoder.encode(scope, StandardCharsets.UTF_8);
    }

}
