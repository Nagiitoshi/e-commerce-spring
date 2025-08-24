package com.nagi.e_commerce_spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nagi.e_commerce_spring.model.Users;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);

    boolean existsByEmail(String email);
}
