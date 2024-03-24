package com.example.auth.repository;

import com.example.auth.model.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<Auth, Long> {

    Auth findByEmailAndPassword(String email, String password);

}
