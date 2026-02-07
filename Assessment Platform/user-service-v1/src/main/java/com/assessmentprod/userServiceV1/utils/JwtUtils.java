package com.assessmentprod.userServiceV1.utils;

//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import java.time.Instant;
//import java.util.Base64;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;

//@Component
public class JwtUtils {

//    private static final String SECRET_KEY = "638CBE3A90E0303BF3808F40F95A7F02A24B4B5D029C954CF553F79E9EF1DC0384BE681C249F1223F6B55AA21DC070914834CA22C8DD98E14A872CA010091ACC";
//
//    public String generateToken(UserDetails userDetails) {
//        Map<String, Object> claims = new HashMap<>();
//
//        return Jwts.builder()
//                .claims(claims)
//                .subject(userDetails.getUsername())
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 100))
//                .signWith(generateKey())
//                .compact();
//    }
//
//
//    private SecretKey generateKey() {
//        byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
//        return Keys.hmacShaKeyFor(decodedKey);
//    }
//
//
//    public String extractUsername(String jwtToken) {
//        Claims claims = getClaims(jwtToken);
//        return claims.getSubject();
//    }
//
//
//    public Claims getClaims(String jwtToken) {
//        Claims claims = Jwts.parser()
//                .verifyWith(generateKey())
//                .build()
//                .parseSignedClaims(jwtToken)
//                .getPayload();
//
//        return claims;
//    }
//
//
//    public boolean isTokenValid(String jwtToken) {
//        Claims claims = getClaims(jwtToken);
//
//        return claims.getExpiration().after(Date.from(Instant.now())); //The expiration should be in the future
//    }
}
