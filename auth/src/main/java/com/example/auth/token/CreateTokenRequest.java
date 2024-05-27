package com.example.auth.token;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class CreateTokenRequest {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    protected CreateTokenRequest() {
    }

    protected CreateTokenRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    protected String getEmail() {
        return email;
    }

    protected String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "CreateTokenRequest [email=" + email + "]";
    }

}
