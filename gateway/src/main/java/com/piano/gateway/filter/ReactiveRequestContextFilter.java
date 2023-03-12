package com.piano.gateway.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

@Component
public class ReactiveRequestContextFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
	serverWebExchange.getResponse().getHeaders().add("custom-header", "Added in ReactiveRequestContextFilter");
	return webFilterChain.filter(serverWebExchange);
    }

}