package com.example.gateway.common;

import java.util.Locale;
import java.util.ResourceBundle;

public class BaseResponse {

    private String code;

    private String message;

    public BaseResponse() {
        super();
    }

    public BaseResponse(String code) {
        super();
        this.code = code;
        this.message = ResourceBundle.getBundle("messages", Locale.getDefault()).getString(code);
    }

    public BaseResponse(String code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public BaseResponse(String code, Locale locale) {
        super();
        this.code = code;
        this.message = ResourceBundle.getBundle("messages", locale).getString(code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}