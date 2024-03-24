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

    protected Mono<TokenResponse> createToken(@NotBlank TokenRequest loginRequest) {
        return apiUtil
                .postLogin(TypeUtil.toMap(loginRequest))
                .switchIfEmpty(Mono.error(() -> new GlobalException("952", null)))
                .flatMap(data -> {
                    try {
                        String refreshToken = tokenRepository.findById(TypeUtil.toLong(((Map<String, Object>) data).get("id"), 0)).orElseThrow().getRefreshToken().toString();
                        String accessToken = jwtUtil.generateAccessToken(refreshToken);
                        return Mono.just(new TokenResponse(refreshToken, accessToken));
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                });
    }

}