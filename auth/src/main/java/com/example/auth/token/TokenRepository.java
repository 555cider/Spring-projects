package com.example.auth.token;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

interface TokenRepository extends R2dbcRepository<Token, Long> {

    @Query("INSERT INTO temp.token (auth_id, refresh_token, issued_at) VALUES ($1, DEFAULT, DEFAULT) RETURNING id")
    Mono<Long> saveWithAuthId(Long authId);

}
