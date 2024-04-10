package com.example.auth.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/auth")
class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping(value = "ping", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<String>> ping() {
        logger.info("ping");
        return Mono.just(ResponseEntity.ok().body("pong"));
    }

    @PostMapping(value = "/login")
    public Mono<ResponseEntity<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        logger.info("loginRequest = {}", loginRequest);
        return authService.login(loginRequest)
                .doOnNext(loginResponse -> logger.info("loginResponse = {}", loginResponse))
                .map(ResponseEntity::ok);
    }

}
