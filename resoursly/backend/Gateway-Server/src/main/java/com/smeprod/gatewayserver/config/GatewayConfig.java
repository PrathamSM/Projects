package com.smeprod.gatewayserver.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpHeaders;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import java.util.Base64;
import java.util.Objects;

@Component
public class GatewayConfig extends AbstractGatewayFilterFactory<GatewayConfig.Config> {


    @Value("${jwt.secret}")
    private String jwtSecret;

    public GatewayConfig() {
        super(Config.class);
    }

    public static class Config {
        private String requiredRole;

        public Config() {}  // Add a no-args constructor

        public Config(String requiredRole) {
            this.requiredRole = requiredRole;
        }

        public String getRequiredRole() {
            return requiredRole;
        }

        public void setRequiredRole(String requiredRole) {
            this.requiredRole = requiredRole;
        }

        @Override
        public String toString() {
            return "Config{requiredRole='" + requiredRole + "'}";
        }
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            System.out.println("Applying GatewayConfig filter...");
            System.out.println("Config Object: " + config);  // Debugging

            if (config.getRequiredRole() == null) {
                return onError(exchange, "Required Role is missing in Gateway config", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            System.out.println("Required Role from Config: " + config.getRequiredRole()); // Debugging

            HttpHeaders headers = exchange.getRequest().getHeaders();
            if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "Missing Authorization Header", HttpStatus.UNAUTHORIZED);
            }

            String token = Objects.requireNonNull(headers.getFirst(HttpHeaders.AUTHORIZATION));
            System.out.println("Received Token: " + token);
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Claims claims;
            try {
                claims = getClaims(token);
            } catch (Exception e) {
                return onError(exchange, "Invalid Token", HttpStatus.UNAUTHORIZED);
            }

            String userRole = claims.get("role", String.class);
            System.out.println("Extracted Role from Token: " + userRole); // Debugging

            if (userRole == null || !userRole.equalsIgnoreCase(config.getRequiredRole())) {
                return onError(exchange, "Access Denied", HttpStatus.FORBIDDEN);
            }

            return chain.filter(exchange);
        };
    }

    public Claims getClaims(String jwtToken) {
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    private SecretKey generateKey() {
        byte[] decodedKey = Base64.getDecoder().decode(jwtSecret);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }
}
