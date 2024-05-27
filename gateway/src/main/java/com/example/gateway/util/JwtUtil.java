package com.example.gateway.util;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    private static final String JOSE_HEADER_TYPE_JWT = "JWT";
    private static final String TOKEN_TYPE = "tokenType";
    private static final String TOKEN_TYPE_ACCESS = "access";
    private static final String ALGORITHM_EC = "EC";
    private static final int TOKEN_STATUS_VALID = 1;
    private static final int TOKEN_STATUS_INVALID = -1;
    private static final int TOKEN_STATUS_MISSING = -2;
    private static final int TOKEN_STATUS_MALFORMED = -3;
    private static final int TOKEN_STATUS_EXPIRED = -4;

    private static String applicationName;

    private static Long accessExpiration;

    @Value("${service.jwt.private-key}")
    private String privateKeyPath;

    @Value("${service.jwt.public-key}")
    private String publicKeyPath;

    private static PrivateKey privateKey;

    private static PublicKey publicKey;

    @Value("${spring.application.name}")
    private void setApplicationName(String applicationName) {
        JwtUtil.applicationName = applicationName;
    }

    @Value("${service.jwt.access-expiration:60 * 60 * 1000L}")
    private void setAccessExpiration(Long accessExpiration) {
        JwtUtil.accessExpiration = accessExpiration;
    }

    @PostConstruct
    public void init() throws Exception {
        URI privateKeyUri = new ClassPathResource(privateKeyPath).getURI();
        logger.info("privateKeyUri = {}", privateKeyUri);
        privateKey = getPrivateKeyFromUri(privateKeyUri);

        URI publicKeyUri = new ClassPathResource(publicKeyPath).getURI();
        logger.info("publicKeyUri = {}", publicKeyUri);
        publicKey = getPublicKeyFromUri(publicKeyUri);
    }

    // private key 읽기
    private static PrivateKey getPrivateKeyFromPath(Path filePath) throws Exception {
        String key = new String(Files.readAllBytes(filePath));
        key = key.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
        logger.info("PRIVATE_KEY = {}", key);
        byte[] keyBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_EC);
        return keyFactory.generatePrivate(keySpec);
    }

    // private key 읽기
    private static PrivateKey getPrivateKeyFromUri(URI uri) throws Exception {
        return getPrivateKeyFromPath(Paths.get(uri));
    }

    // public key 읽기
    private static PublicKey getPublicKeyFromPath(Path filePath) throws Exception {
        String key = new String(Files.readAllBytes(filePath));
        key = key.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");
        logger.info("PUBLIC_KEY = {}", key);
        byte[] keyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_EC);
        return keyFactory.generatePublic(keySpec);
    }

    // public key 읽기
    private static PublicKey getPublicKeyFromUri(URI uri) throws Exception {
        return getPublicKeyFromPath(Paths.get(uri));
    }

    public static String createToken(String refreshToken) {
        Date currentTime = new Date();
        return Jwts.builder()
                .header().type(JOSE_HEADER_TYPE_JWT).and()
                .issuer(applicationName)
                .subject(refreshToken)
                .expiration(new Date(currentTime.getTime() + accessExpiration))
                .issuedAt(currentTime)
                .id(UUID.randomUUID().toString())
                .claim(TOKEN_TYPE, TOKEN_TYPE_ACCESS)
                .signWith(privateKey)
                .compact();
    }

    public static boolean validateToken(String token) {
        return isValidStatus(getTokenStatus(token));
    }

    public static int getTokenStatus(String token) {
        if (token == null) {
            logger.error("TOKEN_STATUS_MISSING");
            return TOKEN_STATUS_MISSING;
        }

        JwtParser parser = Jwts.parser().verifyWith(publicKey).build();
        Claims claims;
        try {
            claims = parser.parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            logger.error("TOKEN_STATUS_EXPIRED (token = {})", token);
            return TOKEN_STATUS_EXPIRED;
        } catch (JwtException e) {
            logger.error("TOKEN_STATUS_MALFORMED (token = {})", token);
            return TOKEN_STATUS_MALFORMED;
        }
        if (!claims.getIssuer().equals(applicationName)
                || claims.getSubject() == null
                || !claims.get(TOKEN_TYPE).equals(TOKEN_TYPE_ACCESS)
                || claims.getId() == null) {
            logger.error("TOKEN_STATUS_INVALID (token = {})", token);
            return TOKEN_STATUS_INVALID;
        }
        return TOKEN_STATUS_VALID;
    }

    public static boolean isValidStatus(int tokenStatus) {
        return tokenStatus > 0;
    }

}
