package com.duoc.semana1.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET_KEY =
            "BancoXYZSecretKeyParaJWT2026BancoXYZ";

    private final SecretKey key =
            Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generarToken(UserDetails userDetails) {

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim(
                        "roles",
                        userDetails.getAuthorities()
                                .stream()
                                .map(authority ->
                                        authority.getAuthority())
                                .toList()
                )
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 1000 * 60 * 60
                        )
                )
                .signWith(key)
                .compact();
    }

    public String obtenerUsername(String token) {

        return obtenerClaims(token)
                .getSubject();
    }

    public boolean esTokenValido(
            String token,
            UserDetails userDetails
    ) {

        String username = obtenerUsername(token);

        return username.equals(userDetails.getUsername())
                && !obtenerClaims(token)
                        .getExpiration()
                        .before(new Date());
    }

    private Claims obtenerClaims(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
