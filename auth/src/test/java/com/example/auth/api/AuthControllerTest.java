package com.example.auth.api;

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
@WebFluxTest(AuthController.class)
class AuthControllerTest {

    private WebTestClient webTestClient;

    @MockBean
    private AuthService authService;

    @BeforeEach
    void setup() {
        webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:8082").build();
    }

    @Test
    void login_InvalidCredentials_ThrowsGlobalException() {

        LoginRequest loginRequest = new LoginRequest("invalid@example.com", "wrongpassword");
        LoginResponse loginResponse = new LoginResponse().withCode("9952").withMessage("Invalid email or password");

        when(this.authService.login(loginRequest)).thenReturn(Mono.error(new GlobalException("9952", "Invalid email or password")));

        this.webTestClient
                .post().uri("/auth/login")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(loginRequest), LoginRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEmpty()
                .jsonPath("$.code").isEqualTo(loginResponse.getCode())
                .jsonPath("$.message").isEqualTo(loginResponse.getMessage());

    }

    @Test()
    void login_ValidCredentials_ReturnsLoginResponse() {

        LoginRequest loginRequest = new LoginRequest("test1@example.com", "password1");
        LoginResponse loginResponse = new LoginResponse(1L);

        when(this.authService.login(loginRequest)).thenReturn(Mono.just(loginResponse));

        this.webTestClient
                .post().uri("/auth/login")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(loginRequest), LoginRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isNumber();

    }

}
