package com.example.auth.token;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/")
class TokenController {

    private static final Logger logger = LoggerFactory.getLogger(TokenController.class);

    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping(value = "ping")
    public Mono<ResponseEntity<String>> ping() {
        logger.info("ping");
        return Mono.just(ResponseEntity.ok().body("pong"));
    }

    @PostMapping(value = "/token")
    public Mono<ResponseEntity<CreateTokenResponse>> createToken(@RequestBody CreateTokenRequest thisRequest) {
        logger.info("CreateTokenRequest = {}", thisRequest);
        return tokenService.createToken(thisRequest)
                .doOnNext(loginResponse -> logger.info("CreateTokenResponse = {}", loginResponse))
                .map(ResponseEntity::ok);
    }

}
