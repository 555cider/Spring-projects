package com.example.gateway.token;

import com.example.gateway.common.BaseException;
import com.example.gateway.util.ApiUtil;
import com.example.gateway.util.JwtUtil;
import com.example.gateway.util.TypeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    @Value("${service.api.auth.base-path}")
    private static String authBasePath;

    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    protected Mono<TokenPostResponse> postToken(TokenPostRequest thisRequest) {
        return ApiUtil
                .callPostApi(authBasePath + "/token", TypeUtil.toMap(thisRequest))
                .doOnNext(authApiResponse -> logger.info("authApiResponse = {}", authApiResponse))
                .flatMap(authApiResponse ->
                        tokenRepository
                                .findById(TypeUtil.toLong(((Map<String, Object>) authApiResponse).get("id"), 0))
                                .cache())
                .map(token -> {
                    String refreshToken = TypeUtil.toString(token.refreshToken(), "");
                    String accessToken = JwtUtil.createToken(refreshToken);
                    return new TokenPostResponse(refreshToken, accessToken);
                })
                .onErrorMap(e -> new BaseException("9951").exception((Exception) e));
    }

    protected Mono<RefreshTokenPostResponse> postRefreshToken(RefreshTokenPostRequest thisRequest) {
        return ApiUtil
                .callPostApi(authBasePath + "/token", TypeUtil.toMap(thisRequest))
                .doOnNext(authApiResponse -> logger.info("authApiResponse = {}", authApiResponse))
                .flatMap(authApiResponse ->
                        tokenRepository
                                .findById(TypeUtil.toLong(((Map<String, Object>) authApiResponse).get("id"), 0))
                                .cache())
                .map(token -> new RefreshTokenPostResponse(TypeUtil.toString(token.refreshToken(), "")))
                .onErrorMap(e -> new BaseException("9951").exception((Exception) e));
    }

    protected Mono<AccessTokenPostResponse> postAccessToken(AccessTokenPostRequest thisRequest) {
        return Mono
                .just(JwtUtil.createToken(thisRequest.getRefreshToken()))
                .doOnNext(accessToken -> logger.info("accessToken = {}", accessToken))
                .map(AccessTokenPostResponse::new)
                .onErrorMap(e -> new BaseException("9951").exception((Exception) e));
    }

}
