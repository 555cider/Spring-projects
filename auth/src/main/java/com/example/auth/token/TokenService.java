package com.example.auth.token;

import com.example.auth.common.GlobalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
class TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    private final AuthRepository authRepository;

    private final TokenRepository tokenRepository;

    public TokenService(AuthRepository authRepository, TokenRepository tokenRepository) {
        this.authRepository = authRepository;
        this.tokenRepository = tokenRepository;
    }

    public Mono<CreateTokenResponse> createToken(CreateTokenRequest createTokenRequest) {
        return authRepository
                .findByEmailAndPassword(createTokenRequest.getEmail(), createTokenRequest.getPassword())
                .switchIfEmpty(Mono.error(new GlobalException("9952", "Invalid email or password")))
                .cache()
                .flatMap(auth -> tokenRepository.saveWithAuthId(auth.id()))
                .map(CreateTokenResponse::new)
                .onErrorMap(e -> {
                    logger.error("error = {}", e.getLocalizedMessage());
                    return new GlobalException("9951", e.getMessage());
                });
    }

}
