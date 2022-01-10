package com.example.user_microservice.repository;

import com.example.user_microservice.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String usernameToFind);
}