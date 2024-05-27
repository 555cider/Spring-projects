package com.example.auth.token;

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
class TokenServiceTest {

    @Mock
    private AuthRepository authRepository;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private TokenService tokenService;

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
        CreateTokenRequest loginRequest = new CreateTokenRequest("test1@example.com", "password1");
        Auth auth = new Auth(1L, loginRequest.getEmail(), loginRequest.getPassword());
        Long tokenId = 1L;
        CreateTokenResponse tokenPostResponse = new CreateTokenResponse(tokenId);
        when(authRepository.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword())).thenReturn(Mono.just(auth));
        when(tokenRepository.saveWithAuthId(auth.id())).thenReturn(Mono.just(tokenId));

        // When
        Mono<CreateTokenResponse> loginResponseMono = tokenService.createToken(loginRequest);

        // Then
        StepVerifier
                .create(loginResponseMono)
                .expectSubscription()
                .thenConsumeWhile(tokenPostResponse::equals)
                .expectComplete()
                .verify();
    }

    /**
     * Test case to verify that login with invalid credentials throws a GlobalException.
     */
    @Test
    public void testLogin_InvalidCredentials_ThrowsGlobalException() {
        // Given
        CreateTokenRequest loginRequest = new CreateTokenRequest("invalid@example.com", "wrongpassword");
        CreateTokenResponse tokenPostResponse = new CreateTokenResponse().withCode("9952").withMessage("Invalid email or password");
        when(authRepository.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword())).thenReturn(Mono.empty());

        // When
        Mono<CreateTokenResponse> loginResponseMono = tokenService.createToken(loginRequest);

        // Then
        StepVerifier
                .create(loginResponseMono)
                .expectErrorMatches(error -> error instanceof GlobalException
                        && tokenPostResponse.getCode().equals((((GlobalException) error).getCode()))
                        && tokenPostResponse.getMessage().equals(error.getMessage()))
                .verify();
    }

}
