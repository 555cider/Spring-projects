package com.example.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class MessageConfig {

    @Bean
    ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setAlwaysUseMessageFormat(true);
        messageSource.setBasename("classpath:/messages/messages");
        // messageSource.setCacheSeconds(60);
        messageSource.setDefaultEncoding("UTF-8");
        // messageSource.setFallbackToSystemLocale(false);
        // messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

}