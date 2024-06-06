package com.example.auth.model;

import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

public class Client implements Serializable {

    private String id;
    private String clientId;
    private Instant clientIdIssuedAt;
    private String clientSecret;
    private Instant clientSecretExpiresAt;
    private String clientName;
    private String clientAuthenticationMethods;
    private String authorizationGrantTypes;
    private String scopes;
    private String clientSettings;
    private String tokenSettings;

    public String getId() {
        return id;
    }

    public Client id(String id) {
        this.id = id;
        return this;
    }

    public String getClientId() {
        return clientId;
    }

    public Client clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public Instant getClientIdIssuedAt() {
        return clientIdIssuedAt;
    }

    public Client clientIdIssuedAt(Instant clientIdIssuedAt) {
        this.clientIdIssuedAt = clientIdIssuedAt;
        return this;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public Client clientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public Instant getClientSecretExpiresAt() {
        return clientSecretExpiresAt;
    }

    public Client clientSecretExpiresAt(Instant clientSecretExpiresAt) {
        this.clientSecretExpiresAt = clientSecretExpiresAt;
        return this;
    }

    public String getClientName() {
        return clientName;
    }

    public Client clientName(String clientName) {
        this.clientName = clientName;
        return this;
    }

    public String getClientAuthenticationMethods() {
        return clientAuthenticationMethods;
    }

    public Client clientAuthenticationMethods(Set<ClientAuthenticationMethod> clientAuthenticationMethods) {
        this.clientAuthenticationMethods = StringUtils.collectionToCommaDelimitedString(
                clientAuthenticationMethods.stream()
                        .map(ClientAuthenticationMethod::getValue)
                        .collect(Collectors.toList()));
        return this;
    }

    public Client clientAuthenticationMethods(String clientAuthenticationMethods) {
        this.clientAuthenticationMethods = clientAuthenticationMethods;
        return this;
    }

    public String getAuthorizationGrantTypes() {
        return authorizationGrantTypes;
    }

    public Client authorizationGrantTypes(Set<AuthorizationGrantType> authorizationGrantTypes) {
        this.authorizationGrantTypes = StringUtils.collectionToCommaDelimitedString(
                authorizationGrantTypes.stream()
                        .map(AuthorizationGrantType::getValue)
                        .collect(Collectors.toList()));
        return this;
    }

    public Client authorizationGrantTypes(String authorizationGrantTypes) {
        this.authorizationGrantTypes = authorizationGrantTypes;
        return this;
    }

    public String getScopes() {
        return scopes;
    }

    public Client scopes(Set<String> scopes) {
        this.scopes = StringUtils.collectionToCommaDelimitedString(scopes);
        return this;
    }

    public Client scopes(String scopes) {
        this.scopes = scopes;
        return this;
    }

    public String getClientSettings() {
        return clientSettings;
    }

    public Client clientSettings(String clientSettings) {
        this.clientSettings = clientSettings;
        return this;
    }

    public String getTokenSettings() {
        return tokenSettings;
    }

    public Client tokenSettings(String tokenSettings) {
        this.tokenSettings = tokenSettings;
        return this;
    }

}
