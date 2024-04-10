package com.example.auth.api;

import com.example.auth.common.GlobalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final AuthRepository authRepository;

    private final TokenRepository tokenRepository;

    public AuthService(AuthRepository authRepository, TokenRepository tokenRepository) {
        this.authRepository = authRepository;
        this.tokenRepository = tokenRepository;
    }

    public Mono<LoginResponse> login(LoginRequest loginRequest) {
        return authRepository
                .findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword())
                .switchIfEmpty(Mono.error(new GlobalException("9952", "Invalid email or password")))
                .cache()
                .flatMap(auth -> tokenRepository.saveWithAuthId(auth.id()))
                .map(LoginResponse::new)
                .onErrorMap(e -> {
                    logger.error("login error = {}", e.getLocalizedMessage());
                    if (e instanceof GlobalException) {
                        return e;
                    }
                    return new GlobalException("9951", e.getMessage());
                });
    }

}
