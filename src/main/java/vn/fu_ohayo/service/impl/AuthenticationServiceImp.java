package vn.fu_ohayo.service.impl;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import vn.fu_ohayo.dto.request.SignInRequest;
import vn.fu_ohayo.dto.response.ExtractTokenResponse;
import vn.fu_ohayo.dto.response.TokenResponse;
import vn.fu_ohayo.dto.response.UserFromProvider;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.Provider;
import vn.fu_ohayo.enums.TokenType;
import vn.fu_ohayo.enums.UserStatus;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.AuthenticationService;
import vn.fu_ohayo.service.JwtService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

@Slf4j(topic = "AUTHENTICATION-SERVICE")
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)

public class AuthenticationServiceImp implements AuthenticationService {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    String googleRedirectUri;

    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    String githubClientId;

    @Value("${spring.security.oauth2.client.registration.github.redirect-uri}")
    String githubRedirectUri;

    @Value("${spring.security.oauth2.client.registration.facebook.client-id}")
    String facebookClientId;

    @Value("${spring.security.oauth2.client.registration.facebook.redirect-uri}")
    String facebookRedirectUri;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    String googleClientSecret;

    @Value("${spring.security.oauth2.client.registration.facebook.client-secret}")
    String facebookClientSecret;
    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    String githubClientSecret;


    final UserRepository userRepository;
    final JwtService jwtService;

    public AuthenticationServiceImp(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    final AuthenticationManager authenticationManager;

    @Override
    public TokenResponse getAccessToken(SignInRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException e) {
            throw new AccessDeniedException(e.getMessage());
        }
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        String accessToken = jwtService.generateAccessToken(user.getUserId(), user.getEmail(), null, Provider.LOCAL);
        String refreshToken = jwtService.generateRefreshToken(user.getUserId(), user.getEmail(), null, Provider.LOCAL);
        return TokenResponse.builder().refreshToken(refreshToken).accessToken(accessToken).build();
    }

    @Override
    public TokenResponse getRefreshToken(String request) {
        log.info("Get refresh token");

        if (!StringUtils.hasLength(request)) {
            throw new AppException(ErrorEnum.REFRESH_TOKEN_NOT_FOUND);
        }

        try {
            ExtractTokenResponse response = jwtService.extractUsername(request, TokenType.REFRESH_TOKEN);

            User user = userRepository.findByEmail(response.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + response.getEmail()));
            String accessToken = jwtService.generateAccessToken(user.getUserId(), user.getEmail(), null, Provider.LOCAL);
            return TokenResponse.builder().accessToken(accessToken).refreshToken(request).build();
        } catch (Exception e) {
            log.error("Error generating refresh token: {}", e.getMessage());
            throw new AppException(ErrorEnum.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public TokenResponse getAccessTokenForSocialLogin(String email, Provider provider) {
        User user = userRepository.findByEmailAndProvider(email, provider)
                .orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        String accessToken = jwtService.generateAccessToken(user.getUserId(), user.getEmail(), null, provider);
        String refreshToken = jwtService.generateRefreshToken(user.getUserId(), user.getEmail(), null, provider);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public boolean extractToken(String token, TokenType type) {
        try {
            var response = jwtService.extractUsername(token, type);
            if (response.getEmail() != null && userRepository.existsByEmail(response.getEmail())) {
                User user = userRepository.findByEmail(response.getEmail()).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
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

            case "github":
                baseUrl = "https://github.com/login/oauth/authorize";
                clientId = githubClientId;
                redirectUri = "http://localhost:8080/auth/code/github";
                scope = "user:email";
                break;
            default:
                throw new IllegalArgumentException("Unsupported OAuth provider: " + type);
        }
        log.info("Generating auth URL for type: {}", type);

        return baseUrl + "?" +
                "client_id=" + clientId +
                "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
                "&response_type=" + responseType +
                "&scope=" + URLEncoder.encode(scope, StandardCharsets.UTF_8);
    }

    public String getAccesTokenFromProvider(String provider, String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        String url;
        switch (provider.toLowerCase()) {
            case "google":
                url = "https://oauth2.googleapis.com/token";
                body.add("client_id", googleClientId);
                body.add("client_secret", googleClientSecret);
                body.add("redirect_uri", googleRedirectUri);
                body.add("grant_type", "authorization_code");
                body.add("code", code);
                break;
            case "facebook":
                url = "https://graph.facebook.com/v12.0/oauth/access_token?" +
                        "client_id=" + facebookClientId +
                        "&client_secret=" + facebookClientSecret +
                        "&redirect_uri=" + facebookRedirectUri +
                        "&code=" + code;
                return restTemplate.getForObject(url, Map.class).get("access_token").toString();
            case "github":
                body.add("client_id", githubClientId);
                body.add("client_secret", githubClientSecret);
                body.add("code", code);
                body.add("redirect_uri", githubRedirectUri);
                url = "https://github.com/login/oauth/access_token";
                break;

            default:
                throw new IllegalArgumentException("Unsupported OAuth provider: " + provider);
        }
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        log.info("Requesting access token from {} with code: {}", provider, code);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
        return response.getBody().get("access_token").toString();
    }

    public UserFromProvider getUserInfoFromProvider(String provider, String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        Provider providerEnum = Provider.GOOGLE;
        String url;
        switch (provider.toLowerCase()) {
            case "google":
                url = "https://www.googleapis.com/oauth2/v3/userinfo";
                break;
            case "facebook":
                providerEnum = Provider.FACEBOOK;
                url = "https://graph.facebook.com/me?fields=email&access_token=" + accessToken;
                break;
            case "github":
                providerEnum = Provider.GITHUB;
                url = "https://api.github.com/user";
                break;
            default:
                throw new IllegalArgumentException("Unsupported provider: " + provider);
        }
        ResponseEntity<Map> response  = null;
        if(providerEnum == Provider.FACEBOOK) {
             response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        }
        else {
              response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        }
        Map<String, Object> userInfo = response.getBody();
        String email = (String) userInfo.get("email");
        boolean userExist = userRepository.existsByEmail(email);
        User user = new User();
        if(userExist) {
            user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
            if(Provider.LOCAL.equals(user.getProvider()) && Provider.GOOGLE.equals(providerEnum)) {
                return UserFromProvider.builder().email(email).isExist(true).build();
            }
        }
        user.setEmail(email);
        user.setProvider(providerEnum);
        userRepository.save(user);
        return UserFromProvider.builder().email(email).isExist(false).build();

    }

}
