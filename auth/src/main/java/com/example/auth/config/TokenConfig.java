package com.example.auth.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.*;

import javax.crypto.spec.SecretKeySpec;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Configuration
public class TokenConfig {

    private static final Logger logger = LoggerFactory.getLogger(TokenConfig.class);
    private static final String JWT_ALGORITHM_RSA = "RSA";
    private static final int JWT_KEY_SIZE = 2048;
    private static final String TOKEN_TYPE_ACCESS = "access_token";
    private static final String TOKEN_TYPE_REFRESH = "refresh_token";

    @Value("${custom.token.issuer:'http://127.0.0.1:9000'}")
    private String jwtIssuer;

    @Value("${custom.token.access-token-expiration:3600}")
    private long accessTimeToLive;

    @Bean
    public OAuth2TokenGenerator<?> tokenGenerator() {
        // JWT
        JwtEncoder jwtEncoder = jwtEncoder(jwkSource());
        JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);
        jwtGenerator.setJwtCustomizer(jwtCustomizer());

        // Access and Refresh Tokens
        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();

        return new DelegatingOAuth2TokenGenerator(jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
    }

    /**
     * Creates a NimbusJwtEncoder with the provided JWKSource.
     * (asymmetric signing algorithm)
     *
     * @param jwkSource The JWKSource to use for signing and verifying JWTs.
     * @return A NimbusJwtEncoder instance.
     */
    @Bean
    public NimbusJwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return context -> {
            JwtClaimsSet.Builder claims = context.getClaims();
            context.getClaims()
                    .issuer(jwtIssuer)
                    .subject(context.getPrincipal().getName())
                    .issuedAt(new Date().toInstant())
                    .expiresAt(new Date().toInstant().plusSeconds(accessTimeToLive))
                    .build();
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                claims.claim("type", TOKEN_TYPE_ACCESS);
            } else if (OAuth2TokenType.REFRESH_TOKEN.equals(context.getTokenType())) {
                claims.claim("type", TOKEN_TYPE_REFRESH);
            }
        };
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(JWT_ALGORITHM_RSA);
            keyPairGenerator.initialize(JWT_KEY_SIZE);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    /**
     * Creates a JwtEncoder that signs JWTs using the provided secret key.
     * (symmetric signing algorithm)
     *
     * @return A JwtEncoder instance.
     */
    public JwtEncoder jwtEncoder() {
        return parameters -> {
            // Decode the secret key from Base64
            byte[] secretKeyBytes = Base64.getDecoder().decode("secret_key");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, "HmacSHA256");

            try {
                // Create a new JWSHeader with HS256 algorithm
                JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

                // Create a new JWTClaimsSet builder and populate it with claims
                JWTClaimsSet.Builder claimsSetBuilder = new JWTClaimsSet.Builder();
                parameters.getClaims().getClaims()
                        .forEach((key, value) -> claimsSetBuilder
                                .claim(key, value instanceof Instant ? Date.from((Instant) value) : value)
                        );
                JWTClaimsSet claimsSet = claimsSetBuilder.build();

                // Create a new SignedJWT and sign it with the secret key
                SignedJWT signedJWT = new SignedJWT(header, claimsSet);
                signedJWT.sign(new MACSigner(secretKeySpec));

                // Build and return a new Jwt instance with the signed JWT
                return Jwt.withTokenValue(signedJWT.serialize())
                        .header("alg", header.getAlgorithm().getName())
                        .header("typ", "JWT")
                        .issuedAt(claimsSet.getIssueTime().toInstant())
                        .expiresAt(claimsSet.getExpirationTime().toInstant())
                        .build();
            } catch (Exception e) {
                throw new IllegalStateException("Error while signing the JWT", e);
            }
        };
    }

}
