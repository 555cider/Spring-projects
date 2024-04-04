package com.example.gateway.api.token;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface TokenRepository extends R2dbcRepository<Token, Long> {

}
