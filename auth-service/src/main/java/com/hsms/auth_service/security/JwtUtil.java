package com.hsms.auth_service.security;

import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.UserDetails;

@Component
public class JwtUtil {

    @Value("${app.jwt-secret}")
    private String secret;

    @Value("${app.jwt-expiration-milliseconds}")
    private long expiration;

    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateToken(UserDetails userDetails, Long userId, List<String> roles) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("userId", userId)
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key())
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public Long extractUserId(String token) {
        Claims claims = Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload();
        return claims.get("userId", Long.class);
    }

    public List<String> extractRoles(String token) {
        Claims claims = Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload();
        return claims.get("roles", List.class);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            Date expiry = Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload().getExpiration();
            return username.equals(userDetails.getUsername()) && !isTokenExpired(expiry);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isTokenExpired(Date expiry) {
        return expiry.before(new Date());
    }
}
