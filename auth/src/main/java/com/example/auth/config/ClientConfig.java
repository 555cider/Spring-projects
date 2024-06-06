package com.example.auth.config;

import com.example.auth.model.Client;
import com.example.auth.repository.ClientRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;

@Configuration
public class ClientConfig {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(ClientRepository clientRepository) {
        this.objectMapper.registerModules(SecurityJackson2Modules.getModules(ClientRepository.class.getClassLoader()));
        this.objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());

        return new RegisteredClientRepository() {
            @Override
            public void save(RegisteredClient registeredClient) {
                clientRepository.save(toEntity(registeredClient));
            }

            @Override
            public RegisteredClient findById(String id) {
                return clientRepository.findById(Long.valueOf(id)).map(this::toObject).block();
            }

            @Override
            public RegisteredClient findByClientId(String clientId) {
                return clientRepository.findByClientId(clientId).map(this::toObject).block();
            }

            private RegisteredClient toObject(Client client) {
                Set<String> clientAuthenticationMethods = StringUtils.commaDelimitedListToSet(client.getClientAuthenticationMethods());
                Set<String> authorizationGrantTypes = StringUtils.commaDelimitedListToSet(client.getAuthorizationGrantTypes());
                Set<String> clientScopes = StringUtils.commaDelimitedListToSet(client.getScopes());
                return RegisteredClient
                        .withId(client.getId())
                        .clientId(client.getClientId())
                        .clientIdIssuedAt(client.getClientIdIssuedAt())
                        .clientSecret(passwordEncoder().encode(client.getClientSecret()))
                        .clientSecretExpiresAt(client.getClientSecretExpiresAt())
                        .clientName(client.getClientName())
                        .clientAuthenticationMethods(authenticationMethods ->
                                clientAuthenticationMethods.forEach(authenticationMethod ->
                                        authenticationMethods.add(new ClientAuthenticationMethod(authenticationMethod))))
                        .authorizationGrantTypes((grantTypes) ->
                                authorizationGrantTypes.forEach(grantType ->
                                        grantTypes.add(new AuthorizationGrantType(grantType))))
                        .scopes((scopes) -> scopes.addAll(clientScopes))
                        .clientSettings(ClientSettings.withSettings(parseMap(client.getClientSettings())).build())
                        .tokenSettings(TokenSettings.withSettings(parseMap(client.getTokenSettings())).build())
                        .build();
            }

            private Client toEntity(RegisteredClient registeredClient) {
                return new Client()
                        .id(registeredClient.getId())
                        .clientId(registeredClient.getClientId())
                        .clientIdIssuedAt(registeredClient.getClientIdIssuedAt())
                        .clientSecret(registeredClient.getClientSecret())
                        .clientSecretExpiresAt(registeredClient.getClientSecretExpiresAt())
                        .clientName(registeredClient.getClientName())
                        .clientAuthenticationMethods(registeredClient.getClientAuthenticationMethods())
                        .authorizationGrantTypes(registeredClient.getAuthorizationGrantTypes())
                        .scopes(registeredClient.getScopes())
                        .clientSettings(writeMap(registeredClient.getClientSettings().getSettings()))
                        .tokenSettings(writeMap(registeredClient.getTokenSettings().getSettings()));
            }

            private Map<String, Object> parseMap(String data) {
                try {
                    return objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {
                    });
                } catch (Exception ex) {
                    throw new IllegalArgumentException(ex.getMessage(), ex);
                }
            }

            private String writeMap(Map<String, Object> data) {
                try {
                    return objectMapper.writeValueAsString(data);
                } catch (Exception ex) {
                    throw new IllegalArgumentException(ex.getMessage(), ex);
                }
            }
        };
    }

}