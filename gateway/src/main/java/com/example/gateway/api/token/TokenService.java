package com.example.gateway.api.token;

import com.example.gateway.common.GlobalException;
import com.example.gateway.util.ApiUtil;
import com.example.gateway.util.JwtUtil;
import com.example.gateway.util.TypeUtil;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    private final TokenRepository tokenRepository;

    @Autowired
    ApiUtil apiUtil;

    @Autowired
    JwtUtil jwtUtil;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    protected Mono<TokenResponse> createToken(@NotBlank TokenRequest tokenRequest) {
        return apiUtil
                .postLogin(TypeUtil.toMap(tokenRequest))
                .doOnNext(loginResponse -> logger.info("loginResponse = {}", loginResponse))
                .flatMap(loginResponse -> tokenRepository
                        .findById(TypeUtil.toLong(((Map<String, Object>) loginResponse).get("id"), 0))
                        .cache())
                .map(token -> {
                    String refreshToken = TypeUtil.toString(token.refreshToken(), "");
                    String accessToken = jwtUtil.generateAccessToken(refreshToken);
                    return new TokenResponse(refreshToken, accessToken);
                })
                .onErrorMap(e -> new GlobalException("9951", (Exception) e));
    }

}
