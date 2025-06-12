package com.Travelrithm.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${spring.jwt.secret}")
    private String secretKey;
    private Key key;
    private final long tokenValidityInMilliseconds = 1000L * 60 * 60 * 24; // 24시간

    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }
    public String generateToken(String userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(userId)         // 유저 식별자 (필요에 따라 email, uuid 등)
                .setIssuedAt(now)           // 토큰 발급 시간
                .setExpiration(expiry)      // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

}
