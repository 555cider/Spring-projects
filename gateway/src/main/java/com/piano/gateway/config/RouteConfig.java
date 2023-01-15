package com.piano.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    RouteLocator routes(RouteLocatorBuilder builder) {
	return builder.routes() //
		.route("admin", p -> p.path("/admin/**").uri("lb://ADMIN"))
		// .route("team", p -> p.path("/team/**").uri("lb://TEAM"))
		.build();
    }

}