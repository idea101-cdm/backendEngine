package com.idea101.backendengine.common.jwt;

import com.idea101.backendengine.common.enums.UserRole;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long expirationMinutes;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expirationMinutes
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMinutes = expirationMinutes;
    }

    public String generateToken(UUID userId, UserRole role, Boolean isVerified, Boolean isActive) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(userId.toString())
                .id(UUID.randomUUID().toString())
                .claim("role", role)
                .claim("isVerified", isVerified)
                .claim("isActive", isActive)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expirationMinutes, ChronoUnit.MINUTES)))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public UUID extractUserId(String token) {
        String subject = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
        return UUID.fromString(subject);
    }

    public UserRole extractRole(String token) {
        return (UserRole) Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role");
    }

    public Boolean extractIsVerified(String token) {
        return (Boolean) Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("isVerified");
    }

    public Boolean extractIsActive(String token) {
        return (Boolean) Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("isActive");
    }
}
