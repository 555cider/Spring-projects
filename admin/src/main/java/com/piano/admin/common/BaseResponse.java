package com.piano.admin.common;

import java.util.Locale;
import java.util.ResourceBundle;

public class BaseResponse {

    private String code;

    private String message;

    private Locale locale;

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

    public Locale getLocale() {
	return locale;
    }

    public void setLocale(Locale locale) {
	this.locale = locale;
    }

    public BaseResponse() {
	super();
    }

    public BaseResponse(String code) {
	super();
	this.code = code;
	this.message = ResourceBundle.getBundle("messages", Locale.getDefault()).getString(code);
	this.locale = Locale.getDefault();
    }

    public BaseResponse(String code, String message) {
	super();
	this.code = code;
	this.message = message;
	this.locale = Locale.getDefault();
    }

    public BaseResponse(String code, Locale locale) {
	super();
	this.code = code;
	this.message = ResourceBundle.getBundle("messages", locale).getString(code);
	this.locale = locale;
    }

    public BaseResponse(String code, String message, Locale locale) {
	super();
	this.code = code;
	this.message = message;
	this.locale = locale;
    }

}