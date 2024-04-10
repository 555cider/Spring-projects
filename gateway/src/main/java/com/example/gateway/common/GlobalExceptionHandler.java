package com.example.gateway.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

@Configuration
@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<BaseResponse> handleGlobalException(GlobalException ex, Locale locale) {
        String message;
        if (ex.getMessage() == null || "".equals(ex.getMessage())) {
            message = messageSource.getMessage(ex.getCode(), null, locale);
        } else {
            message = ex.getLocalizedMessage();
        }
        return ResponseEntity.ok().body(new BaseResponse(ex.getCode(), message));
    }

}
