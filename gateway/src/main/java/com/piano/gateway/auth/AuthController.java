package com.piano.gateway.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.piano.gateway.common.GlobalException;

import reactor.core.publisher.Mono;

@RestController
public class AuthController {

    @Autowired
    MessageSource messageSource;

    @Autowired
    private AuthService authService;

    @GetMapping("/auth")
    public Mono<ResponseEntity<Auth>> getAuth(@RequestParam("id") String hashKey) {
	return authService.get(hashKey) //
		.map(ResponseEntity::ok) //
		.onErrorMap(e -> {
		    if (e instanceof GlobalException) {
			return new GlobalException("951", messageSource.getMessage("error.951", null, null));
		    }
		    return new GlobalException("951", e.getMessage());
		});
    }

    @PostMapping("/auth")
    public Mono<ResponseEntity<Long>> postAuth(@RequestBody Auth auth) {
	return this.authService.post(auth) //
		.map(ResponseEntity::ok) //
		.onErrorMap(e -> {
		    if (e instanceof GlobalException) {
			return new GlobalException("951", messageSource.getMessage("error.951", null, null));
		    }
		    return new GlobalException("951", e.getMessage());
		});
    }

    @DeleteMapping("/auth")
    public Mono<ResponseEntity<Boolean>> deleteAuth(@RequestBody Auth auth) {
	return this.authService.delete(auth) //
		.map(ResponseEntity::ok) //
		.onErrorMap(e -> {
		    if (e instanceof GlobalException) {
			return new GlobalException("951", messageSource.getMessage("error.951", null, null));
		    }
		    return new GlobalException("951", e.getMessage());
		});
    }

}