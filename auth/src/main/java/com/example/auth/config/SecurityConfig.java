package com.example.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String tokenEndpoint;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String tokenIntrospectionEndpoint;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String tokenRevocationEndpoint;

    /**
     * A Spring Security filter chain for the OAuth2 core components and protocol endpoints.
     *
     * @see <a href="https://docs.spring.io/spring-authorization-server/reference/core-model-components.html">Core Components</a>
     * @see <a href="https://docs.spring.io/spring-authorization-server/reference/protocol-endpoints.html">Protocol Endpoints</a>
     */
    @Bean
    @Order(1)

    public SecurityFilterChain oauthSecurityFilterChain(HttpSecurity http)
            throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                // .registeredClientRepository(registeredClientRepository) // (REQUIRED) => ClientConfig
                // .authorizationService(authorizationService) // => AuthorizationServerConfig
                .authorizationServerSettings(authorizationServerSettings()) // (REQUIRED)
                // .tokenGenerator(tokenGenerator) // => TokenConfig
        ;
        return http.build();
    }

    // A Spring Security filter chain for authentication.
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
            throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer(issuerUri)
                .tokenEndpoint("/oauth2/v1/token")
                .tokenIntrospectionEndpoint("/oauth2/v1/introspect")
                .tokenRevocationEndpoint("/oauth2/v1/revoke")
                .jwkSetEndpoint("/oauth2/v1/jwks")
                .build();
    }

}
