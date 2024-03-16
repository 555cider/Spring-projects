package com.example.gateway.auth;

import com.example.gateway.common.BaseResponse;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "auth")
public class Auth extends BaseResponse {

    @Id
    private String id;

    private String email;

    private String password;

    public Auth() {
        super();
    }

    public Auth(String email, String password) {
        super();
        this.email = email;
        this.password = password;
    }

    public Auth(String id, String email, String password) {
        super();
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}