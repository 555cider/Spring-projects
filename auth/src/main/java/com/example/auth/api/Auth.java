package com.example.auth.api;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "auth", schema = "temp")
record Auth(
        @Id
        Long id,

        String email,

        String password
) {
}
