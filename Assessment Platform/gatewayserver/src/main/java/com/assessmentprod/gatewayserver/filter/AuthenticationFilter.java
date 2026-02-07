package com.assessmentprod.gatewayserver.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import com.assessmentprod.gatewayserver.filter.RouteValidator.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

//@Component
//public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config>{
//
//    public AuthenticationFilter() {
//        super(Config.class);
//    }
//
//    @Override
//    public GatewayFilter apply(Config config) {
//        return(((exchange, chain) -> {
//            return chain.filter(exchange);
//        }));
//    }
//
//    public static class Config {
//
//    }
//}
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config>{

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final RouteValidator routeValidator;

    //updation for 3-role auth
    private final Map<String, Set<String>> roleAccess = new HashMap<>();
    //updation for 3-role auth



    //updation for 3-role auth
    private void setRoleAccess() {
        roleAccess.put("ROLE_ADMIN",Set.of(
                "GET:/questions/**", "POST:/questions/**", "PUT:/questions/**", "DELETE:/questions/**",
                "GET:/submissions/**", "POST:/submissions/**", "PUT:/submissions/**", "DELETE:/submissions/**",
                "GET:/evaluations/**", "POST:/evaluations/**", "PUT:/evaluations/**", "DELETE:/evaluations/**",
                "GET:/tests/**", "POST:/tests/**", "PUT:/tests/**", "DELETE:/tests/**",
                "GET:/assessments/**", "POST:/assessments/**", "PUT:/assessments/**", "DELETE:/assessments/**",
                "GET:/users/**", "PUT:/users/*/role"
        ));

//        roleAccess.put("ROLE_HIRING_MANAGER", Set.of("GET:/user/profile"));
//        roleAccess.put("ROLE_USER", Set.of("GET:/tests"));
        roleAccess.put("ROLE_HIRING_MANAGER", Set.of("GET:/questions","GET:/tests", "GET:/assessments","GET:/submissions","GET:/evaluations"));
        roleAccess.put("ROLE_USER", Set.of("GET:/users/profile", "PUT:/users/profile","POST:/submissions/**"));

    }
    //updation for 3-role auth



    public AuthenticationFilter(RouteValidator routeValidator) {
        super(Config.class);
        this.routeValidator = routeValidator;

        setRoleAccess();
    }


    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                String token = extractToken(exchange.getRequest().getHeaders());
                if (token == null || !validateToken(token)) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }

                Claims claims = getClaimsFromToken(token);
                List<String> roles = claims.get("roles", List.class);

                logger.info("Roles: {}", roles);

//                if (roles == null || (!roles.contains("ROLE_ADMIN") && exchange.getRequest().getPath().toString().startsWith("/questions"))){
//                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
//                    return exchange.getResponse().setComplete();
//                }

                String path = exchange.getRequest().getPath().toString();
                String method = exchange.getRequest().getMethod().name();

                logger.info("Method type: {}", method);
                logger.info("Path: {}", path);

//                if (roles == null || (!roles.contains("ROLE_ADMIN") && (
//                        (path.startsWith("/questions") && !roles.contains("ROLE_ADMIN")) ||
//                                (path.startsWith("/tests") && (
//                                        (!roles.contains("ROLE_ADMIN") && !roles.contains("ROLE_HIRING_MANAGER")) ||
//                                                (roles.contains("ROLE_HIRING_MANAGER") && !method.equals("GET"))
//                                ))
//                ))) {
//                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
//                    return exchange.getResponse().setComplete();
//                }

                if(!roleAccessStatus(roles, method, path)) {
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);

                    //sending cust res
                    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    byte[] bytes = "{\"message\": \"You don't have access for this URL\"}".getBytes(StandardCharsets.UTF_8);

                    return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
                }


            }
            return chain.filter(exchange);
        };
    }

    private String extractToken(HttpHeaders headers) {
        String bearerToken = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String jwtToken) {
//        Claims claims = getClaims(jwtToken);
//
//        return claims.getExpiration().after(Date.from(Instant.now())); //The expiration should be in the future

        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(jwtToken);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public Claims getClaimsFromToken(String jwtToken) {
        Claims claims = Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();

        return claims;
    }

    public Claims getClaims(String jwtToken) {
        Claims claims = Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();

        return claims;
    }

    private SecretKey generateKey() {
        byte[] decodedKey = Base64.getDecoder().decode(jwtSecret);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    private SecretKey getSigningKey() {
        byte[] decodedKey = Base64.getDecoder().decode(jwtSecret);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    private boolean roleAccessStatus(List<String> roles, String method, String path) {
        if (roles == null) {
            return false;
        }

        for(String role : roles) {
            Set<String> allowedPaths = roleAccess.get(role);

            if(allowedPaths != null) {
                for (String allowedPath : allowedPaths) {
                    String[] parts = allowedPath.split(":");
                    String allowedMethod = parts[0];
                    String allowedPathPattern = parts[1];

                    if(role.equals("ROLE_ADMIN") && allowedMethod.equals("PUT") && allowedPath.contains("/role")) {
                        return true;
                    }

                    if(method.equals(allowedMethod) && pathMatches(allowedPathPattern, path)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean pathMatches(String pattern, String path) {
        if (pattern.endsWith("/**")) {
            String basePattern = pattern.substring(0, pattern.length() - 3);
            return path.startsWith(basePattern);
        } else if (pattern.endsWith("/*")) {
            String basePattern = pattern.substring(0, pattern.length() - 2);
            return path.startsWith(basePattern) && !path.substring(basePattern.length()).contains("/");
        } else {
            return pattern.equals(path);
        }
    }

    public static class Config {
        // Configuration properties if needed
    }
}