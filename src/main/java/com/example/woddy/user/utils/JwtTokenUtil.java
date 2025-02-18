package com.example.woddy.user.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenUtil {

    private static Key key;

    // 비밀키를 초기화합니다.
    @Value("${jwt.secret}")
    public void setJwtSecret(String jwtSecret) {
        key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // 토큰을 생성합니다.
    public String generateToken(long id, long expiredTimeMs) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(id));
        claims.put("userId", id);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰을 검증합니다.
    public void verifyToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            Date expiration = claims.getBody().getExpiration();
            if (expiration.before(new Date())) {
                log.debug("JWT token has expired");
            }

        } catch (JwtException | IllegalArgumentException e) {
            log.debug("JWT token is invalid: " + e.getMessage());
        }
    }


    // 토큰에서 사용자 ID를 추출합니다.
    public String getUid(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public static long getUserIdFromToken(String token) {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        return claims.getBody().get("userId", Long.class);
    }

}


