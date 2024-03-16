package com.example.gateway.config;

import com.ulisesbocchio.jasyptspringboot.encryptor.SimpleGCMConfig;
import com.ulisesbocchio.jasyptspringboot.encryptor.SimpleGCMStringEncryptor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig {

    @Bean(name = "jasyptStringEncryptor")
    protected StringEncryptor stringEncryptor() {
        SimpleGCMConfig config = new SimpleGCMConfig();
        // add "secret_key.b64" to ".gitginore" in production
        config.setSecretKeyLocation("classpath:secret_key.b64");
        // add "-Djasypt.encryptor.password={password}" to VM options in production
        config.setSecretKeyPassword("jasypt-password");
        return new SimpleGCMStringEncryptor(config);
    }

}