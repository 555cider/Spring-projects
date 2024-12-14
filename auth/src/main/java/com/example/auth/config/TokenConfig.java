package com.example.auth.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class TokenConfig {

    private static final Logger logger = LoggerFactory.getLogger(TokenConfig.class);

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @Bean
    public OAuth2TokenGenerator<?> tokenGenerator() throws IOException, ParseException {
        // JWT
        JwtEncoder jwtEncoder = jwtEncoder(jwkSource());
        JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);

        jwtGenerator.setJwtCustomizer(jwtCustomizer());

        // Access and Refresh Tokens
        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();

        return new DelegatingOAuth2TokenGenerator(jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
    }

    @Bean
    public NimbusJwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        return NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return context -> {
            JwsHeader.Builder headers = context.getJwsHeader();
            JwtClaimsSet.Builder claims = context.getClaims();

            if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
                Set<String> authorities = context.getPrincipal()
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet());
                claims.claim("authorities", authorities);
            }

            claims.claim("client_id", context.getRegisteredClient().getClientId());
        };
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() throws IOException, ParseException {
        Resource jwksFile = new ClassPathResource("./.well-known/jwks.json");
        String jwksContent = new String(FileCopyUtils.copyToByteArray(jwksFile.getInputStream()));
        JWKSet jwkSet = JWKSet.parse(jwksContent);
        return new ImmutableJWKSet<>(jwkSet);
    }

}
