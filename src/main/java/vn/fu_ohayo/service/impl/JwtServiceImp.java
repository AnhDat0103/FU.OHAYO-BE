package vn.fu_ohayo.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.response.ExtractTokenResponse;
import vn.fu_ohayo.enums.Provider;
import vn.fu_ohayo.enums.TokenType;
import vn.fu_ohayo.service.JwtService;

import javax.sound.midi.InvalidMidiDataException;
import java.security.Key;
import java.text.ParseException;
import java.util.*;
import java.util.function.Function;
@Slf4j(topic = "JwtServiceImp")
@Service
public class JwtServiceImp implements JwtService {
    @Value("${jwt.accessKey}")
    private String accessKey;

    @Value("${jwt.refreshKey}")
    private String refreshKey;

    @Value("${jwt.expiryMinutes}")
    private long expiryMinutes;

    @Value("${jwt.expiryDay}")
    private long expiryDay;

    @Override
    public String generateAccessToken(int userId, String email, Collection<? extends GrantedAuthority> authorities, Provider provider) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);
        claims.put("role", authorities);
        claims.put("provider", provider);
        return generateAccessToken(claims, email);
    }

    @Override
    public String generateRefreshToken(int userId, String email, Collection<? extends GrantedAuthority> authorities, Provider provider) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);
        claims.put("role", authorities);
        claims.put("provider", provider);
        return generateRefreshToken(claims, email);
    }

    @Override
    public ExtractTokenResponse extractUsername(String token, TokenType type) {
        log.info("Extracting username from token: {}", token);
        Claims claims = extractAllClaims(token, type);
        String email = claims.getSubject();
        String provider = claims.get("provider", String.class);
        Provider providerEnum = Provider.valueOf(provider);
        return new ExtractTokenResponse(email, providerEnum);
    }

    private Claims extractAllClaims(String token, TokenType type) {
        try {
            return Jwts.parserBuilder().setSigningKey(getKey(type)).build().parseClaimsJws(token).getBody();
        }catch (SignatureException | ExpiredJwtException e) {
            throw new AccessDeniedException(e.getMessage());
        }
    }

    private String generateAccessToken(Map<String, Object> claims, String email) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * expiryMinutes))
                .signWith(getKey(TokenType.ACCESS_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }
    private String generateRefreshToken(Map<String, Object> claims, String email) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * expiryDay))
                .signWith(getKey(TokenType.REFRESH_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey(TokenType type) {
        switch (type) {
            case ACCESS_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode("D0oN9tEJlPfctdhy2mHJlMpiDiBB7QSYQ4PqNADNhI4="));
            }
            case REFRESH_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode("rciTbyStKhFLnSCnbgJnQK5a1l7vVKZIKWDZZXnlVCE="));
            }
            default -> throw new RuntimeException();
        }
    }
}
