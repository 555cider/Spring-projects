package com.example.gateway.token;

import com.example.gateway.common.BaseResponse;

import java.util.Locale;

public class AccessTokenPostResponse extends BaseResponse {

    private final String accessToken;

    protected AccessTokenPostResponse(String accessToken) {
        super();
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return String.format(Locale.KOREA, "accessToken = %s", accessToken);
    }

}
