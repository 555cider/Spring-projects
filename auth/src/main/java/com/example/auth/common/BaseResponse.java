package com.example.auth.common;

import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.http.HttpStatus;

public class BaseResponse {

    private int code = 100;

    private String message = "OK";

    private Object data;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public <T extends BaseResponse> T withCode(int code) {
        this.code = code;
        return (T) this;
    }

    public <T extends BaseResponse> T withMessage(String message) {
        this.message = message;
        return (T) this;
    }

    public BaseResponse() {
        super();
    }

    public BaseResponse(int code) {
        super();
        this.code = code;
        this.message = ResourceBundle.getBundle("messages", Locale.getDefault()).getString(String.valueOf(code));
    }

    public BaseResponse(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public BaseResponse(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BaseResponse(HttpStatus httpStatus) {
        this.code = httpStatus.value();
        this.message = httpStatus.getReasonPhrase();
    }

    public BaseResponse(HttpStatus httpStatus, Object data) {
        this.code = httpStatus.value();
        this.message = httpStatus.getReasonPhrase();
        this.data = data;
    }

    public BaseResponse(HttpStatus httpStatus, String error, String errorDescription) {
        this.code = httpStatus.value();
        this.message = httpStatus.getReasonPhrase();
        this.data = new HashMap<String, String>() {
            {
                put("error", error);
                put("error_description", errorDescription);
            }
        };
    }

}
