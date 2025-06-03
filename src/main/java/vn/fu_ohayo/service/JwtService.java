package vn.fu_ohayo.service;

import org.springframework.security.core.GrantedAuthority;
import vn.fu_ohayo.dto.response.ExtractTokenResponse;
import vn.fu_ohayo.enums.Provider;
import vn.fu_ohayo.enums.TokenType;

import java.util.Collection;

public interface JwtService {
    String generateAccessToken(int userId, String email, Collection<? extends GrantedAuthority> authorities);
    String generateRefreshToken(int userId, String email, Collection<? extends GrantedAuthority> authorities);
    ExtractTokenResponse extractUserInformation(String token, TokenType type);
}
