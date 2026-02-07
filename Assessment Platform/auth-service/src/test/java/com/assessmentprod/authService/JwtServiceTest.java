package com.assessmentprod.authService;

import static org.junit.jupiter.api.Assertions.*;

import com.assessmentprod.authService.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private static final String SECRET_KEY = "638CBE3A90E0303BF3808F40F95A7F02A24B4B5D029C954CF553F79E9EF1DC0384BE681C249F1223F6B55AA21DC070914834CA22C8DD98E14A872CA010091ACC";
    private SecretKey secretKey;

    @BeforeEach
    void setUp() {
        // Manually initialize JwtService due to the lack of Spring context in unit testing.
        jwtService = new JwtService();
        byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
        secretKey = Keys.hmacShaKeyFor(decodedKey);
    }

    @Test
    void generateToken_ShouldReturnValidToken() {
        String username = "testuser";
        List<String> roles = List.of("ROLE_USER");

        String token = jwtService.generateToken(username, roles);

        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(username, claims.getSubject());
        assertEquals(roles, claims.get("roles"));
        assertTrue(claims.getExpiration().after(new Date()));
    }

    @Test
    void getClaims_ShouldReturnClaimsFromValidToken() {
        String username = "testuser";
        List<String> roles = List.of("ROLE_USER");
        String token = jwtService.generateToken(username, roles);

        Claims claims = jwtService.getClaims(token);

        assertEquals(username, claims.getSubject());
        assertEquals(roles, claims.get("roles"));
        assertTrue(claims.getExpiration().after(new Date()));
    }

    @Test
    void isTokenValid_ShouldReturnTrueForValidToken() {
        String username = "testuser";
        List<String> roles = List.of("ROLE_USER");
        String token = jwtService.generateToken(username, roles);

        assertTrue(jwtService.isTokenValid(token));
    }

    public Claims getClaims(String jwtToken) {
        try {
            return Jwts.parser()
                    .setSigningKey(generateKey())
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            // Log or handle expired token as needed
            return e.getClaims();  // Optionally return claims from expired token
        } catch (Exception e) {
            // Log or handle other exceptions as needed
            return null;  // Or throw a custom exception if preferred
        }
    }
    private SecretKey generateKey() {
        byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(decodedKey);
    }



}
