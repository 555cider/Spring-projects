package com.piano.gateway.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/auth")
    public Mono<Auth> getAuth(@RequestParam("id") String hashKey) {
	return this.authService.get(hashKey);
    }

    @PostMapping("/auth")
    public Mono<Long> postAuth(@RequestBody Auth auth) {
	return this.authService.post(auth);
    }

    @DeleteMapping("/auth")
    public Mono<Boolean> deleteAuth(@RequestBody Auth auth) {
	return this.authService.delete(auth);
    }

}