package com.example.auth.common;

import org.springframework.http.HttpStatus;

public class GlobalException extends Throwable {

    private int code;

    private String message;

    private Exception exception;

    public GlobalException(int code) {
        this.code = code;
    }

    public GlobalException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public GlobalException(int code, Exception exception) {
        this.code = code;
        this.exception = exception;
    }

    public GlobalException(int code, String message, Exception exception) {
        this.code = code;
        this.message = message;
        this.exception = exception;
    }

    public GlobalException(HttpStatus httpStatus) {
        this.code = httpStatus.value();
        this.message = httpStatus.getReasonPhrase();
    }

    public GlobalException(HttpStatus httpStatus, Exception exception) {
        this.code = httpStatus.value();
        this.message = httpStatus.getReasonPhrase();
        this.exception = exception;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Exception getException() {
        return exception;
    }

}
