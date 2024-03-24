package com.example.auth.model;

import jakarta.persistence.*;

@Entity
@Table(name = "auth", schema = "temp")
public class Auth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Auth [id=" + id + ", email=" + email + "]";
    }

}
