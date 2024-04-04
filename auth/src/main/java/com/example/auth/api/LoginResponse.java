package com.example.auth.api;

import com.example.auth.common.BaseResponse;

class LoginResponse extends BaseResponse {

    private Long id;

    public LoginResponse() {
        super();
    }

    public LoginResponse(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "LoginResponse{ id=" + id + " }";
    }

}