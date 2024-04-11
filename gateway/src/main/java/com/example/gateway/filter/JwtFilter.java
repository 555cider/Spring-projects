package com.example.gateway.filter;

import com.example.gateway.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

@Order(2)
@Component
public class JwtFilter implements WebFilter {

    public static final String AUTH_HEADER_PREFIX = "Bearer ";

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    //    @Value("${service.jwt.skip.path}")
    private static final String[] PATHS_WITHOUT_AUTH = {"/public/", "/token/", "/actuator/", "/favicon.ico"};

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        if (Stream.of(PATHS_WITHOUT_AUTH).anyMatch(path::contains)) {
            logger.info("Skip validating the token in header. path = {}", path);
            return chain.filter(exchange);
        }

        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (token != null && token.startsWith(AUTH_HEADER_PREFIX)) {
            if (jwtUtil.isValidAccessToken(token.substring(7))) {
                return chain.filter(exchange);
            }

            logger.error("Invalid Token");
            return onError(exchange, HttpStatus.FORBIDDEN);
        }

        logger.error("No Authorization header");
        return onError(exchange, HttpStatus.UNAUTHORIZED);
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

}
