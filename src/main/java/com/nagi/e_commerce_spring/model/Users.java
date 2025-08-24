package com.nagi.e_commerce_spring.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.nagi.e_commerce_spring.model.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Users {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Email(message = "Email should be valid!!")
    @NotBlank(message = "Email is mandatory!!")
    private String email;

    
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters!!")
    @NotBlank(message = "Password is mandatory!!")
    private String password;

    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters!!")
    @NotBlank(message = "Username is mandatory!!")
    private String username;

    @NotBlank(message = "Phone Number is mandatory!!")
    @Size(min = 10, max = 15, message = "Phone Number must be between 10 and 15 characters!!")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "created_in", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdIn;
}
