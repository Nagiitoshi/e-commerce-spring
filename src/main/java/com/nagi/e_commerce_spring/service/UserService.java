package com.nagi.e_commerce_spring.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagi.e_commerce_spring.dto.user.UserRequestDTO;
import com.nagi.e_commerce_spring.dto.user.UserResponseDTO;
import com.nagi.e_commerce_spring.exception.BusinessException;
import com.nagi.e_commerce_spring.exception.ResourceNotFoundException;
import com.nagi.e_commerce_spring.exception.ValidationException;
import com.nagi.e_commerce_spring.model.Users;
import com.nagi.e_commerce_spring.model.enums.Role;
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
    public UserResponseDTO createUser(UserRequestDTO request) {
        Users user = Users.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(request.getPassword()) 
                .role(Role.USER) 
                .createdIn(LocalDateTime.now())
                .build();

        validateUserData(user);
        Users saved = userRepository.save(user);

        return toResponse(saved);
    }

    // Login user
    public Users loginUser(String email, String password) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        if (!user.getPassword().equals(password)) {
            throw new BusinessException("Invalid password for user: " + email);
        }
        return user;
    }

    // Update profile
    public UserResponseDTO updateProfile(Long userId, UserRequestDTO request) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setUsername(request.getUsername());
        user.setPhoneNumber(request.getPhoneNumber());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(request.getPassword());
        }

        Users updated = userRepository.save(user);
        return toResponse(updated);
    }

    // Delete user
    public void deleteUser(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }

    private void validateUserData(Users user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new BusinessException("Email already in use.");
        }

        if (user.getPassword().length() < 8) {
            throw new ValidationException("Password must be at least 8 characters long.");
        }
    }

    private UserResponseDTO toResponse(Users user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole().name());
    }

}
