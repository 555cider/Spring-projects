package com.example.gateway.util;

import com.example.gateway.common.BaseException;
import io.netty.channel.ChannelOption;
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

    private static final WebClient webClient = WebClient
            .builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();

    public static Mono<Object> callGetApi(String url) {
        return webClient
                .get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        Mono.error(new BaseException("9911").message("Call api error (GET " + url + "). Status code: " + clientResponse.statusCode().value())))
                .bodyToMono(Object.class);
    }

    public static Mono<Object> callPostApi(String url, Map<String, Object> paramMap) {
        return webClient
                .post()
                .uri(url)
                .body(BodyInserters.fromValue(paramMap))
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        Mono.error(new BaseException("9911").message("Call api error (POST " + url + "). Status code: " + clientResponse.statusCode().value())))
                .bodyToMono(Object.class)
                .timeout(Duration.ofSeconds(5));
    }

}
