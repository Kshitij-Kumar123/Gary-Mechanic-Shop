package com.example.user_microservice.repository;

import com.example.user_microservice.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String roleNameToFind);
}