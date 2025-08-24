package com.nagi.e_commerce_spring.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagi.e_commerce_spring.model.Users;
import com.nagi.e_commerce_spring.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Get user by email
    public Users getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    // Register user
    public Users createUser(Users user) {
        Users newUser = Users.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .createdIn(LocalDateTime.now())
                .build();

        validateUserData(newUser);

        return userRepository.save(newUser);
    }

    // Login user
    public Users loginUser(String email, String password) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password for user: " + email);
        }
        return user;
    }

    // Update profile
    public Users updateProfile(Long id, Users userDetails) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());
        user.setUsername(userDetails.getUsername());
        user.setPhoneNumber(userDetails.getPhoneNumber());
        user.setCreatedIn(LocalDateTime.now());

        return userRepository.save(user);
    }

    // Delete user
    public void deleteUser(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.delete(user);
    }

    private void validateUserData(Users user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use.");
        }

        if (user.getPassword().length() < 8) {
            throw new RuntimeException("Password must be at least 8 characters long.");
        }
    }
}
