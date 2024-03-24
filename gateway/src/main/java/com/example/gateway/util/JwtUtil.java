package com.example.gateway.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${spring.application.name}")
    private String ISSUER;

    @Value("${service.jwt.access.subject}")
    private String ACCESS_TOKEN_SUBJECT;

    // accessToken 만료 시간
    @Value("${service.jwt.access.expiration}")
    private Long ACCESS_TOKEN_EXPIRATION_DURATION;

    @Value("${service.jwt.secret-key}")
    private String secretKey;

    // refreshToken 검증을 위한 시크릿 키
    private SecretKey REFRESH_TOKEN_SECRET_KEY;

    @PostConstruct
    private void init() {
        REFRESH_TOKEN_SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
    }

    public String generateAccessToken(String refreshToken) {
        return Jwts.builder()
                .subject(ACCESS_TOKEN_SUBJECT)
                .issuer(ISSUER)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_DURATION))
                .claim("refreshToken", refreshToken)
                .signWith(REFRESH_TOKEN_SECRET_KEY)
                .compact();
    }

    // accessToken 유효성 검증
    public boolean isValid(String accessToken) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(REFRESH_TOKEN_SECRET_KEY).build()
                    .parseSignedClaims(accessToken).getPayload();
            return claims.getIssuer().equals(ISSUER)
                    && claims.getSubject().equals(ACCESS_TOKEN_SUBJECT)
                    && claims.getExpiration().after(new Date());
        } catch (ExpiredJwtException ex) {
            logger.error("JWT expired", ex);
        } catch (IllegalArgumentException ex) {
            logger.error("Token is null, empty or only whitespace");
        } catch (MalformedJwtException ex) {
            logger.error("JWT is invalid", ex);
        } catch (UnsupportedJwtException ex) {
            logger.error("JWT is not supported", ex);
        }
        return false;
    }

}
