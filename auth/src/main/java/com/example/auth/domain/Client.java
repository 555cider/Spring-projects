package com.example.auth.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Table(name = "client", value = "client", schema = "my_schema")
public class Client {

    @Id
    private Long id;

    @Column
    private String clientId;

    @Column
    private Instant clientIdIssuedAt;

    @Column
    private String clientSecret;

    @Column
    private Instant clientSecretExpiresAt;

    @Column
    private String clientName;

    @Column
    private String clientAuthenticationMethods;

    @Column
    private String authorizationGrantTypes;

    @Column
    private String redirectUris;

    @Column
    private String postLogoutRedirectUris;

    @Column
    private String scopes;

    @Column
    private String clientSettings;

    @Column
    private String tokenSettings;

    public Long getId() {
        return id;
    }

    public String getClientId() {
        return clientId;
    }

    public Instant getClientIdIssuedAt() {
        return clientIdIssuedAt;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public Instant getClientSecretExpiresAt() {
        return clientSecretExpiresAt;
    }

    public String getClientName() {
        return clientName;
    }

    public Set<ClientAuthenticationMethod> getClientAuthenticationMethods() {
        if (clientAuthenticationMethods == null) {
            return Set.of();
        }
        return Arrays
                .stream(clientAuthenticationMethods.split(" "))
                .map(this::resolveClientAuthenticationMethod)
                .collect(Collectors.toSet());
    }

    public Set<AuthorizationGrantType> getAuthorizationGrantTypes() {
        if (authorizationGrantTypes == null) {
            return Set.of();
        }
        return Arrays
                .stream(authorizationGrantTypes.split(" "))
                .map(this::resolveAuthorizationGrantType)
                .collect(Collectors.toSet());
    }

    public Set<String> getRedirectUris() {
        if (redirectUris == null) {
            return Set.of();
        }
        return Set.of(redirectUris.split(" "));
    }

    public Set<String> getPostLogoutRedirectUris() {
        if (postLogoutRedirectUris == null) {
            return Set.of();
        }
        return Set.of(postLogoutRedirectUris.split(" "));
    }

    public Set<String> getScopes() {
        if (scopes == null) {
            return Set.of();
        }
        return Set.of(scopes.split(" "));
    }

    public String getClientSettings() {
        return clientSettings;
    }

    public String getTokenSettings() {
        return tokenSettings;
    }

    public Client() {
    }

    public Client(Long id, String clientId, Instant clientIdIssuedAt, String clientSecret, Instant clientSecretExpiresAt, String clientName, Set<ClientAuthenticationMethod> clientAuthenticationMethods, Set<AuthorizationGrantType> authorizationGrantTypes, Set<String> redirectUris, Set<String> postLogoutRedirectUris, Set<String> scopes, String clientSettings, String tokenSettings) {
        this.id = id;
        this.clientId = clientId;
        this.clientIdIssuedAt = clientIdIssuedAt;
        this.clientSecret = clientSecret;
        this.clientSecretExpiresAt = clientSecretExpiresAt;
        this.clientName = clientName;
        this.clientAuthenticationMethods = clientAuthenticationMethods
                .stream()
                .map(ClientAuthenticationMethod::getValue)
                .collect(Collectors.joining(" "));
        this.authorizationGrantTypes = authorizationGrantTypes
                .stream()
                .map(AuthorizationGrantType::getValue)
                .collect(Collectors.joining(" "));
        this.redirectUris = redirectUris == null ? null : String.join(" ", redirectUris);
        this.postLogoutRedirectUris = postLogoutRedirectUris == null ? null : String.join(" ", postLogoutRedirectUris);
        this.scopes = scopes == null ? null : String.join(" ", scopes);
        this.clientSettings = clientSettings;
        this.tokenSettings = tokenSettings;
    }

    public Client(RegisteredClient registeredClient) {
        this.id = Long.valueOf(registeredClient.getId());
        this.clientId = registeredClient.getClientId();
        this.clientIdIssuedAt = registeredClient.getClientIdIssuedAt();
        this.clientSecret = null; // registeredClient.getClientSecret();
        this.clientSecretExpiresAt = registeredClient.getClientSecretExpiresAt();
        this.clientName = registeredClient.getClientName();
        this.clientAuthenticationMethods = registeredClient.getClientAuthenticationMethods()
                .stream()
                .map(ClientAuthenticationMethod::getValue)
                .collect(Collectors.joining(" "));
        this.authorizationGrantTypes = registeredClient.getAuthorizationGrantTypes()
                .stream()
                .map(AuthorizationGrantType::getValue)
                .collect(Collectors.joining(" "));
        this.redirectUris = registeredClient.getRedirectUris() == null
                ? null
                : String.join(" ", registeredClient.getRedirectUris());
        this.postLogoutRedirectUris = registeredClient.getPostLogoutRedirectUris() == null
                ? null
                : String.join(" ", registeredClient.getPostLogoutRedirectUris());
        this.scopes = registeredClient.getScopes() == null
                ? null
                : String.join(" ", registeredClient.getScopes());
        this.clientSettings = String.valueOf(registeredClient.getClientSettings().getSettings());
        this.tokenSettings = String.valueOf(registeredClient.getTokenSettings().getSettings());
    }

    public RegisteredClient toRegisteredClient() {
        return RegisteredClient
                .withId(String.valueOf(this.id))
                .clientId(this.clientId)
                .clientIdIssuedAt(this.clientIdIssuedAt)
                .clientSecret(this.clientSecret)
                .clientSecretExpiresAt(this.clientSecretExpiresAt)
                .clientName(this.clientName)
                .clientAuthenticationMethods(methods -> methods.addAll(
                        Arrays.stream(this.clientAuthenticationMethods.split(" "))
                                .map(this::resolveClientAuthenticationMethod)
                                .collect(Collectors.toSet())))
                .authorizationGrantTypes(types -> types.addAll(
                        Arrays.stream(this.authorizationGrantTypes.split(" "))
                                .map(this::resolveAuthorizationGrantType)
                                .collect(Collectors.toSet())))
                .redirectUris((uris) -> uris.addAll(List.of(this.redirectUris.split(" "))))
                // .postLogoutRedirectUris((uris) -> uris.addAll(this.postLogoutRedirectUris.split(" ")))
                .scopes((scopes) -> scopes.addAll(List.of(this.scopes.split(" "))))
                // .clientSettings(ClientSettings.withSettings(client.getClientSettings()).build())
                // .tokenSettings(TokenSettings.withSettings(client.getTokenSettings()).build())
                .build();
    }

    private ClientAuthenticationMethod resolveClientAuthenticationMethod(String clientAuthenticationMethod) {
        if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
        } else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_POST;
        } else if (ClientAuthenticationMethod.NONE.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.NONE;
        }
        return new ClientAuthenticationMethod(clientAuthenticationMethod);
    }

    private AuthorizationGrantType resolveAuthorizationGrantType(String authorizationGrantType) {
        if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.AUTHORIZATION_CODE;
        } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.CLIENT_CREDENTIALS;
        } else if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.REFRESH_TOKEN;
        }
        return new AuthorizationGrantType(authorizationGrantType);
    }

}
