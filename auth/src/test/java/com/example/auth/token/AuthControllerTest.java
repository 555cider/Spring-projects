package com.example.auth.token;

import com.example.auth.common.GlobalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(TokenController.class)
class AuthControllerTest {

    private WebTestClient webTestClient;

    @MockBean
    private TokenService tokenService;

    @BeforeEach
    void setup() {
        webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:8082").build();
    }

    @Test
    void login_InvalidCredentials_ThrowsGlobalException() {

        CreateTokenRequest loginRequest = new CreateTokenRequest("invalid@example.com", "wrongpassword");
        CreateTokenResponse tokenPostResponse = new CreateTokenResponse().withCode("9952").withMessage("Invalid email or password");

        when(this.tokenService.createToken(loginRequest)).thenReturn(Mono.error(new GlobalException("9952", "Invalid email or password")));

        this.webTestClient
                .post().uri("/auth/login")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(loginRequest), CreateTokenRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEmpty()
                .jsonPath("$.code").isEqualTo(tokenPostResponse.getCode())
                .jsonPath("$.message").isEqualTo(tokenPostResponse.getMessage());

    }

    @Test()
    void login_ValidCredentials_ReturnsLoginResponse() {

        CreateTokenRequest loginRequest = new CreateTokenRequest("test1@example.com", "password1");
        CreateTokenResponse tokenPostResponse = new CreateTokenResponse(1L);

        when(this.tokenService.createToken(loginRequest)).thenReturn(Mono.just(tokenPostResponse));

        this.webTestClient
                .post().uri("/auth/login")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(loginRequest), CreateTokenRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isNumber();

    }

}
