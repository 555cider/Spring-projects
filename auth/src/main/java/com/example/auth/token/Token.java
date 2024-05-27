package com.example.auth.token;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.UUID;

@Table(name = "token", schema = "temp")
record Token(
        @Id
        Long id,

        Long authId,

        UUID refreshToken,

        @CreatedDate
        LocalDate issuedAt
) {
}
