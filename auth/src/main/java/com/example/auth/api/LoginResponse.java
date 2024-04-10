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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginResponse that = (LoginResponse) o;
        return id.equals(that.id);
    }

}