package com.example.gateway.redis;

import com.example.gateway.common.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Locale;

@RestController("redis")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    @Autowired
    MessageSource messageSource;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/auth")
    public Mono<ResponseEntity<Auth>> getAuth(@RequestParam("id") String hashKey) {
        return authService.get(hashKey)
                .map(ResponseEntity::ok)
                .onErrorMap(e -> {
                    if (e instanceof BaseException) {
                        return new BaseException("9951").message(messageSource.getMessage("9951", null, Locale.KOREAN));
                    }
                    return new BaseException("9951").message(e.getMessage());
                });
    }

    @PostMapping("/auth")
    public Mono<ResponseEntity<Long>> postAuth(@RequestBody Auth auth) {
        return this.authService.post(auth)
                .map(ResponseEntity::ok)
                .onErrorMap(e -> {
                    if (e instanceof BaseException) {
                        return new BaseException("9951").message(messageSource.getMessage("9951", null, Locale.KOREAN));
                    }
                    return new BaseException("9951").message(e.getMessage());
                });
    }

    @DeleteMapping("/auth")
    public Mono<ResponseEntity<Boolean>> deleteAuth(@RequestBody Auth auth) {
        return this.authService.delete(auth)
                .map(ResponseEntity::ok)
                .onErrorMap(e -> {
                    if (e instanceof BaseException) {
                        return new BaseException("9951").message(messageSource.getMessage("9951", null, Locale.KOREAN));
                    }
                    return new BaseException("9951").message(e.getMessage());
                });
    }

}