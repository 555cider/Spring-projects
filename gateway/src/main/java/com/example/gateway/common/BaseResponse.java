package com.example.gateway.common;

import java.util.Locale;
import java.util.ResourceBundle;

public class BaseResponse {

    private String code = "200";

    private String message = "OK";

    private Object data = null;

    public BaseResponse() {
        super();
    }

    public BaseResponse(String code) {
        super();
        this.code = code;
        this.message = ResourceBundle.getBundle("messages", Locale.getDefault()).getString(code);
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public <T extends BaseResponse> T code(String code) {
        this.code = code;
        return (T) this;
    }

    public <T extends BaseResponse> T message(String message) {
        this.message = message;
        return (T) this;
    }

    public <T extends BaseResponse> T data(Object data) {
        this.data = data;
        return (T) this;
    }

}

