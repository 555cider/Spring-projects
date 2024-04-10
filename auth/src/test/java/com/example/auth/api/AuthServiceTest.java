package com.example.auth.api;

import com.example.auth.common.GlobalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthRepository authRepository;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        authRepository.deleteAll();
        tokenRepository.deleteAll();
    }

    /**
     * Test case to verify that login with valid credentials returns a LoginResponse.
     */
    @Test
    public void testLogin_ValidCredentials_ReturnsLoginResponse() {
        // Given
        LoginRequest loginRequest = new LoginRequest("test1@example.com", "password1");
        Auth auth = new Auth(1L, loginRequest.getEmail(), loginRequest.getPassword());
        Long tokenId = 1L;
        LoginResponse loginResponse = new LoginResponse(tokenId);
        when(authRepository.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword())).thenReturn(Mono.just(auth));
        when(tokenRepository.saveWithAuthId(auth.id())).thenReturn(Mono.just(tokenId));

        // When
        Mono<LoginResponse> loginResponseMono = authService.login(loginRequest);

        // Then
        StepVerifier
                .create(loginResponseMono)
                .expectSubscription()
                .thenConsumeWhile(loginResponse::equals)
                .expectComplete()
                .verify();
    }

    /**
     * Test case to verify that login with invalid credentials throws a GlobalException.
     */
    @Test
    public void testLogin_InvalidCredentials_ThrowsGlobalException() {
        // Given
        LoginRequest loginRequest = new LoginRequest("invalid@example.com", "wrongpassword");
        LoginResponse loginResponse = new LoginResponse().withCode("9952").withMessage("Invalid email or password");
        when(authRepository.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword())).thenReturn(Mono.empty());

        // When
        Mono<LoginResponse> loginResponseMono = authService.login(loginRequest);

        // Then
        StepVerifier
                .create(loginResponseMono)
                .expectErrorMatches(error -> error instanceof GlobalException
                        && loginResponse.getCode().equals((((GlobalException) error).getCode()))
                        && loginResponse.getMessage().equals(error.getMessage()))
                .verify();
    }

}
