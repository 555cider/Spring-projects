package com.example.auth.token;

import com.example.auth.common.BaseResponse;

class CreateTokenResponse extends BaseResponse {

    private Long id;

    public CreateTokenResponse() {
        super();
    }

    public CreateTokenResponse(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "TokenPostResponse{ id=" + id + " }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateTokenResponse that = (CreateTokenResponse) o;
        return id.equals(that.id);
    }

}