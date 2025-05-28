package vn.fu_ohayo.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.CompleteProfileRequest;
import vn.fu_ohayo.dto.request.InitialRegisterRequest;
import vn.fu_ohayo.dto.request.SignInRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.AuthUrlResponse;
import vn.fu_ohayo.dto.response.TokenResponse;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.UserStatus;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.MailService;
import vn.fu_ohayo.service.impl.AuthenticationServiceImp;
import vn.fu_ohayo.service.impl.UserServiceImp;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

  MailService mailService;
  UserServiceImp userService;
  UserRepository userRepository;
  AuthenticationServiceImp authenticationService;

    @PostMapping
    public ResponseEntity<ApiResponse<InitialRegisterRequest>> registerInit(@RequestBody InitialRegisterRequest initialRegisterRequest) {
        userService.registerInitial(initialRegisterRequest);
        return ResponseEntity.ok(
                ApiResponse.<InitialRegisterRequest>builder()
                        .code("200")
                        .status("OK")
                        .message("User registered successfully")
                        .data(initialRegisterRequest)
                        .build()
        );
    }
//    @PostMapping("/google")
//    public ResponseEntity<?> googleLogin(@RequestBody GoogleUserData googleData) {
//        Optional<User> existingUser = userRepository.findByEmail(googleData.getEmail());
//
//        User user;
//        if (existingUser.isPresent()) {
//            user = existingUser.get();
//        } else {
//            user = new User();
//            user.setEmail(googleData.getEmail());
//            user.setName(googleData.getName());
//            user.setProvider("google");
//            userRepository.save(user);
//        }
//
//        String jwt = jwtService.generateToken(user); // tự viết hoặc dùng thư viện
//        return ResponseEntity.ok(Map.of("token", jwt));
//    }
@GetMapping("/social-login")
public ResponseEntity<AuthUrlResponse> socialAuth(@RequestParam("login_type") String type) {
    String url = authenticationService.generateAuthURL(type);
    AuthUrlResponse response = new AuthUrlResponse(url);
    return ResponseEntity.ok(response);
}

    @GetMapping
    public ResponseEntity<ApiResponse<String>> checkUserStatus(@RequestParam("email") String email) {
        var user = userRepository.findByEmail(email);

        if (user == null ) {
            throw new IllegalArgumentException();
        }
        if (user.getStatus().equals(UserStatus.ACTIVE)){
            return ResponseEntity.ok(
                    ApiResponse.<String>builder()
                            .code("200")
                            .status("OK")
                            .message("User registered")
                            .data("Registered")
                            .build()
            );
        }

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .code("200")
                        .status("Failed")
                        .message("User registered")
                        .data("Not Active")
                        .build());
    }

    @PostMapping("/complete-profile")
    public ApiResponse<String> completeProfile(@RequestParam String email, @RequestBody CompleteProfileRequest completeProfileRequest) {
        userService.completeProfile(completeProfileRequest, email);
        return ApiResponse.<String>builder()
                .code("200")
                .status("OK")
                .message("Profile completed successfully")
                .data("Profile completed successfully")
                .build();
    }

    @PostMapping("/login")
        public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody SignInRequest signInRequest) {
            TokenResponse tokenResponse = authenticationService.getAccessToken(signInRequest);

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokenResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60 * 60 * 24 * 7)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(ApiResponse.<TokenResponse>builder()
                        .code("200")
                        .status("OK")
                        .message("User logged in successfully")
                        .data(new TokenResponse(tokenResponse.getAccessToken(), null)) // KHÔNG gửi refreshToken trong body
                        .build());
        }
    @GetMapping("/user")
        public ResponseEntity<ApiResponse<User>> getUserByToken(Authentication authentication) {
            if(authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body(
                        ApiResponse.<User>builder()
                                .code("401")
                                .status("Unauthorized")
                                .message("User not authenticated")
                                .data(null)
                                .build()
                );
            }
            String email = authentication.getName();
            var user = userRepository.findByEmail(email);
            if (user == null) {
                return ResponseEntity.status(404).body(
                        ApiResponse.<User>builder()
                                .code("404")
                                .status("Not Found")
                                .message("User not found")
                                .data(null)
                                .build()
                );
            }
            return ResponseEntity.ok(
                    ApiResponse.<User>builder()
                            .code("200")
                            .status("OK")
                            .message("User retrieved successfully")
                            .data(user)
                            .build()
            );
    }
}
