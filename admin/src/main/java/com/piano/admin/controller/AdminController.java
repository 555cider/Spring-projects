package com.piano.admin.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piano.admin.common.GlobalException;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    ReloadableResourceBundleMessageSource messageSource;

    private static final Logger logger = LogManager.getLogger(AdminController.class);

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<String>> index() {
	logger.info("=== === === Entered AdminController");
	String responseBody = "Ha!! Ha!! Ha!!";
	return Mono.just(ResponseEntity.ok() //
		.header("custom-header2", "Added in adminController") //
		.body(responseBody)) //
		.onErrorMap(e -> {
		    if (e instanceof GlobalException) {
			return new GlobalException("951", messageSource.getMessage("error.951", null, null));
		    }
		    return new GlobalException("951", e.getMessage());
		});
    }

}
