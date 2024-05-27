package com.example.gateway.common;

import java.util.Locale;
import java.util.ResourceBundle;

public class BaseException extends Exception {

    private String code;

    private String message;

    private Exception exception;

    public BaseException() {
        super();
    }

    public BaseException(String code) {
        this.code = code;
        this.message = ResourceBundle.getBundle("messages", Locale.getDefault()).getString(code);
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Exception getException() {
        return exception;
    }

    public BaseException code(String code) {
        this.code = code;
        return this;
    }

    public BaseException message(String message) {
        this.message = message;
        return this;
    }

    public BaseException exception(Exception exception) {
        this.exception = exception;
        return this;
    }

}
