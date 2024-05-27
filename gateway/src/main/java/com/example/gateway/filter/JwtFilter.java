package com.example.gateway.filter;

import com.example.gateway.util.JwtUtil;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Order(2)
@Component
public class JwtFilter implements GlobalFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    private static final String AUTH_HEADER_PREFIX = "Bearer ";

    @Value("${service.jwt.skip-path-regex}")
    private String skipPathRegex;

    private List<String> skipPathRegexList;

    @PostConstruct
    public void init() {
        skipPathRegexList = Arrays.asList(skipPathRegex.split(",\\s*"));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        if (skipPathRegexList.stream().anyMatch(path::matches)) {
            logger.info("Skip validating the token in header. path = {}", path);
            return chain.filter(exchange);
        }

        String authorization = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.startsWith(AUTH_HEADER_PREFIX)) {
            logger.error("Authorization header is missing");
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        if (!JwtUtil.validateToken(authorization.substring(7))) {
            logger.error("Authorization header is invalid");
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        return chain.filter(exchange);
    }

}
