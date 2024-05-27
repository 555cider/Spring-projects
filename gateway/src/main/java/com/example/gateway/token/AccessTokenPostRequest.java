package com.example.gateway.token;

import jakarta.validation.constraints.NotBlank;

public class AccessTokenPostRequest {

    @NotBlank(message = "refreshToken is required")
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    @Override
    public String toString() {
        return "CreateAccessTokenRequest [refreshToken=" + refreshToken + "]";
    }

}
