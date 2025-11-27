package com.shkvlnc.bujo_app.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // ✅ Secure random 256-bit key for HS256
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // ✅ Token expiration (15 minutes)
    private static final long EXPIRATION = 1000 * 60 * 15;

    /**
     * Generate JWT token for a given user.
     */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // email
                .claim("roles", userDetails.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extract email (subject) from JWT token.
     */
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Validate token against user details.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Check if token is expired.
     */
    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }

    /**
     * Debug helper: verify password hash at startup.
     */
    @PostConstruct
    public void printEncodedPassword() {
        boolean matches = new BCryptPasswordEncoder()
                .matches("demo123", "$2a$10$miWOQ97EQpY0oIdUjsaYkuAi0LTa6yqHcBhWu0Mh25hbkFJmeCVpe");
        System.out.println("Password check for demo123: " + matches);
    }
}
