package com.hsms.apigateway.jwtutil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.List;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    public static final String SECRET="MySecretKeyMySecretKeyMySecretKey12345MySecretKeyMySecretKeyMySecretKey12345";

    public boolean validateToken(String token) {
        Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token);
        return true;
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    
    public Long extractUserId(String token) {

        Claims claims = Jwts.parser()
                .verifyWith((SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("userId", Long.class);
    }
    
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {

        Claims claims = Jwts.parser()
                .verifyWith((SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        List<?> roles = claims.get("roles", List.class);

        if (roles == null) {
            return List.of();
        }

        return roles.stream()
                .map(String::valueOf)
                .toList();
    }
}