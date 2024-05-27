package com.example.gateway.token;

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
    public Mono<ResponseEntity<TokenPostResponse>> postToken(@RequestBody TokenPostRequest thisRequest) {
        logger.info("TokenPostRequest = {}", thisRequest);
        return tokenService
                .postToken(thisRequest)
                .doOnNext(tokenResponse -> logger.info("TokenPostResponse = {}", tokenResponse))
                .map(ResponseEntity::ok);
    }

    @PostMapping("/access")
    public Mono<ResponseEntity<AccessTokenPostResponse>> postAccessToken(@RequestBody AccessTokenPostRequest thisRequest) {
        logger.info("AccessTokenPostRequest = {}", thisRequest);
        return tokenService
                .postAccessToken(thisRequest)
                .doOnNext(tokenResponse -> logger.info("AccessTokenPostResponse = {}", tokenResponse))
                .map(ResponseEntity::ok);
    }

    @PostMapping("/refresh")
    public Mono<ResponseEntity<RefreshTokenPostResponse>> postRefreshToken(@RequestBody RefreshTokenPostRequest thisRequest) {
        logger.info("RefreshTokenPostRequest = {}", thisRequest);
        return tokenService
                .postRefreshToken(thisRequest)
                .doOnNext(tokenResponse -> logger.info("RefreshTokenPostResponse = {}", tokenResponse))
                .map(ResponseEntity::ok);
    }

}
