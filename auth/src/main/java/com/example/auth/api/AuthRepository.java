package com.example.auth.api;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

interface AuthRepository extends R2dbcRepository<Auth, Long> {

    Mono<Auth> findByEmailAndPassword(String email, String password);

}
