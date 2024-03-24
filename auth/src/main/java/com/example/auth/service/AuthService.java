package com.example.auth.service;

import com.example.auth.common.GlobalException;
import com.example.auth.model.Auth;
import com.example.auth.model.LoginRequest;
import com.example.auth.model.LoginResponse;
import com.example.auth.model.Token;
import com.example.auth.repository.AuthRepository;
import com.example.auth.repository.TokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final AuthRepository authRepository;

    private final TokenRepository tokenRepository;

    public AuthService(AuthRepository authRepository, TokenRepository tokenRepository) {
        this.authRepository = authRepository;
        this.tokenRepository = tokenRepository;
    }

    public Mono<LoginResponse> login(LoginRequest loginRequest) throws GlobalException {
        Auth auth = authRepository.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        if (auth == null) {
            throw new GlobalException("952");
        }

        Token token = tokenRepository.saveAndFlush(new Token(auth));
        return Mono.just(new LoginResponse(token.getId()));
    }

}
