package com.example.auth.common;

import java.util.Locale;
import java.util.ResourceBundle;

public class BaseResponse {

    private String code = "200";

    private String message = "OK";

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

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public <T extends BaseResponse> T withCode(String code) {
        this.code = code;
        return (T) this;
    }

    public <T extends BaseResponse> T withMessage(String message) {
        this.message = message;
        return (T) this;
    }

}
