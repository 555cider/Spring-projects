package com.example.gateway.token;

import com.example.gateway.common.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Locale;

public class RefreshTokenPostResponse extends BaseResponse {

    @JsonProperty("refresh_token")
    private final String refreshToken;

    protected RefreshTokenPostResponse(String refreshToken) {
        super();
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return String.format(Locale.KOREA, "refreshToken = %s", refreshToken);
    }

}
