package com.wtc.backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Utilitário para geração e validação de tokens JWT.
 *
 * O token é gerado no login (AuthService) e validado em cada
 * requisição pelo JwtAuthFilter.
 *
 * Payload do token:
 * - sub: e-mail do operador
 * - operatorId: ID MongoDB do operador
 * - iat: issued at
 * - exp: expiração (configurável em app.jwt.expiration-ms)
 */
@Component
public class JwtUtils {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    @Value("${app.jwt.refresh-expiration-ms:604800000}")
    private long refreshExpirationMs;

    public String generateToken(String email, String operatorId) {
        return Jwts.builder()
                .subject(email)
                .claim("operatorId", operatorId)
                .claim("type", "access")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(String email, String operatorId) {
        return Jwts.builder()
                .subject(email)
                .claim("operatorId", operatorId)
                .claim("type", "refresh")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean isRefreshToken(String token) {
        return "refresh".equals(parseClaims(token).get("type", String.class));
    }

    /**
     * Extrai o e-mail (subject) do token.
     */
    public String getEmailFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * Extrai o operatorId do token.
     */
    public String getOperatorIdFromToken(String token) {
        return parseClaims(token).get("operatorId", String.class);
    }

    /**
     * Valida o token — retorna true se válido e não expirado.
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
