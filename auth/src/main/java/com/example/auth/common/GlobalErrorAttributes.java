package com.example.auth.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Locale;
import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Autowired
    ReloadableResourceBundleMessageSource messageSource;

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest serverRequest, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(serverRequest, options);
        Throwable error = getError(serverRequest);
        if (error instanceof GlobalException) {
            errorAttributes.put("code", ((GlobalException) error).getCode());
            errorAttributes.put("message", messageSource.getMessage("error." + ((GlobalException) error).getCode(), null, Locale.getDefault()));
            errorAttributes.remove("path");
            errorAttributes.remove("error");
        }
        return errorAttributes;
    }

}