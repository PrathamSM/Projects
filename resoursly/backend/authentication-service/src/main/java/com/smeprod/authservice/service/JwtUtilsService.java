package com.smeprod.authservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtilsService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private long EXPIRATION_TIME = 3600000;

    public String generateToken(String email, Long userId, String role) {
        return Jwts.builder()
                .claim("userId", userId)  // ✅ Save userId
                .claim("role", role)      // ✅ Save role
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(generateKey())
                .compact();
    }

    public Claims getClaims(String jwtToken) {
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    public Long extractUserId(String token) {
        return getClaims(token).get("userId", Long.class);
    }


    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }


    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }



    private SecretKey generateKey() {
        byte[] decodedKey = Base64.getDecoder().decode(jwtSecret);
        return Keys.hmacShaKeyFor(decodedKey);
    }


    public boolean validateToken(String token, String username) {
        try {
            return extractUsername(token).equals(username) && !isTokenExpired(token);
        }
        catch(Exception e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }


}
