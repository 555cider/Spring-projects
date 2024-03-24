package com.example.auth.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;
import java.util.UUID;

@DynamicInsert
@Entity
@Table(name = "token", schema = "temp")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "auth_id", referencedColumnName = "id")
    private Auth auth;

    @Column(nullable = false)
    private UUID refreshToken;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private LocalDateTime issuedAt;

    public Token(Auth auth) {
        this.auth = auth;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public UUID getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(UUID refreshToken) {
        this.refreshToken = refreshToken;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    @PrePersist
    protected void onCreate() {
        setRefreshToken(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return "Token{" +
                "id=" + id +
                ", auth=" + auth +
                ", refreshToken=" + refreshToken +
                ", issuedAt=" + issuedAt +
                '}';
    }

}
