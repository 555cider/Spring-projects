package com.example.gateway.util;

import com.example.gateway.common.GlobalException;
import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Map;

@Component
public class ApiUtil {

    private static final HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000)
            .responseTimeout(Duration.ofMillis(3000));

    private final WebClient webClient = WebClient
            .builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();

    @Value("${service.api.base-url.auth}")
    private String authBaseUrl;

    public Mono<String> callGetApi(String url) {
        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> callPostApi(String url, Map<String, Object> paramMap) {
        return webClient
                .post()
                .uri(url)
                .body(BodyInserters.fromValue(paramMap))
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new GlobalException("error.951", null)))
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(5));
    }

    public Mono<Object> postLogin(Map<String, Object> paramMap) {
        return webClient
                .post()
                .uri(authBaseUrl + "/login")
                .body(BodyInserters.fromValue(paramMap))
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new GlobalException("error.951", null)))
                .bodyToMono(Object.class)
//                .timeout(Duration.ofSeconds(5))
                ;
    }

}
