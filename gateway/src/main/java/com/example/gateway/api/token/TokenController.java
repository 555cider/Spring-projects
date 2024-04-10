package com.example.gateway.api.token;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "token")
public class TokenController {

    private static final Logger logger = LoggerFactory.getLogger(TokenController.class);

    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping({"", "/"})
    public Mono<ResponseEntity<TokenResponse>> postToken(@RequestBody TokenRequest tokenRequest) {
        logger.info("tokenRequest = {}", tokenRequest);
        return tokenService
                .createToken(tokenRequest)
                .doOnNext(tokenResponse -> logger.info("tokenResponse = {}", tokenResponse))
                .map(ResponseEntity::ok);
    }

}
