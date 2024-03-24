package com.example.gateway.api.token;

import com.example.gateway.common.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Locale;

public class TokenResponse extends BaseResponse {

    @JsonProperty("refresh_token")
    private final String refreshToken;

    @JsonProperty("access_token")
    private final String accessToken;

    protected TokenResponse(String refreshToken, String accessToken) {
        super();
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return String.format(Locale.KOREA, "refreshToken = %s, accessToken = %s", refreshToken, accessToken);
    }

}
