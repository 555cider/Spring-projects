package com.example.admin.controller;

import com.example.admin.common.GlobalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    ReloadableResourceBundleMessageSource messageSource;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<String>> index(ServerHttpRequest request) {
        logger.info("=== === === Someone has Entered AdminController. {}", request.getHeaders());
        String responseBody = "Ha!! Ha!! Ha!!";
        return Mono.just(ResponseEntity.ok() //
                        .header("custom-header2", "Added in adminController") //
                        .body(responseBody)) //
                .onErrorMap(e -> new GlobalException("951", e.getMessage()));
    }

}
