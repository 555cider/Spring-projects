package com.example.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    public SecurityConfig(TokenConfig tokenConfig) {
        this.tokenConfig = tokenConfig;
    }

    private static final String[] PERMITTED_URL = {
            "/actuator/**",
            "/resources/**",
            "/public/**",
            "/oauth2",
            "/about"
    };

    private final TokenConfig tokenConfig;

    /**
     * Defines the security filter chain for the server.
     *
     * @param http the ServerHttpSecurity object
     * @return the SecurityWebFilterChain object
     */
    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)

                // Define authorization rules
                .authorizeExchange(request -> request
                        .pathMatchers(PERMITTED_URL).permitAll() // PERMITTED_URL 는 인증  불요
                        .pathMatchers("/test/**").hasAuthority("SCOPE_test") // test 권한 필요
                        .pathMatchers("/real/**").hasAuthority("SCOPE_real") // real 권한 필요
                        .anyExchange().authenticated() // 나머지 모든 요청은 인증 필요
                )

                // Configure OAuth2 Resource Server
                .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec
                        .jwt(jwtSpec -> jwtSpec
                                // Validate JWT signature and decode it to generate a Jwt object
                                .jwtDecoder(tokenConfig.jwtDecoder())
                                // Convert Jwt object to an Authentication object and register it in the SecurityContext.
                                .jwtAuthenticationConverter(tokenConfig.jwtAuthenticationConverter())
                        ))
                .build();
    }

}
