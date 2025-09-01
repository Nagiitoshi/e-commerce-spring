package com.nagi.e_commerce_spring.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nagi.e_commerce_spring.dto.user.UserRequestDTO;
import com.nagi.e_commerce_spring.dto.user.UserResponseDTO;
import com.nagi.e_commerce_spring.exception.BusinessException;
import com.nagi.e_commerce_spring.exception.ResourceNotFoundException;
import com.nagi.e_commerce_spring.model.Users;
import com.nagi.e_commerce_spring.model.enums.Role;
import com.nagi.e_commerce_spring.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private Users user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = Users.builder()
                .id(1L)
                .username("nagi")
                .email("nagi@email.com")
                .password("12345678")
                .role(Role.USER)
                .phoneNumber("123456789")
                .build();
    }

    // Get user by email
    @Test
    void testGetUserByEmail_Success() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Users found = userService.getUserByEmail(user.getEmail());

        assertEquals(user.getId(), found.getId());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void testGetUserByEmail_NotFound() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserByEmail(user.getEmail()));
    }

    // Create user
    @Test
    void testCreateUser_Success() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(Users.class))).thenReturn(user);

        UserResponseDTO created = userService.createUser(user);

        assertEquals(user.getEmail(), created.getEmail());
        verify(userRepository, times(1)).save(any(Users.class));
    }

    @Test
    void testCreateUser_EmailAlreadyUsed() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(BusinessException.class, () -> userService.createUser(user));
    }

    @Test
    void testCreateUser_PasswordTooShort() {
        user.setPassword("123");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> userService.createUser(user));
    }

    // Login
    @Test
    void testLoginUser_Success() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Users logged = userService.loginUser(user.getEmail(), user.getPassword());

        assertEquals(user.getId(), logged.getId());
    }

    @Test
    void testLoginUser_InvalidPassword() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(BusinessException.class,
                () -> userService.loginUser(user.getEmail(), "wrongpass"));
    }

    // Update profile
    @Test
    void testUpdateProfile_Success() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(Users.class))).thenReturn(user);

        UserRequestDTO request = UserRequestDTO.builder()
                .username("updated")
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .build();

        UserResponseDTO updated = userService.updateProfile(user.getId(), request);

        assertEquals("updated", updated.getUsername());
    }

    @Test
    void testUpdateProfile_NotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        UserRequestDTO request = UserRequestDTO.builder()
                .username("updated")
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .build();

        assertThrows(ResourceNotFoundException.class,
                () -> userService.updateProfile(user.getId(), request));
    }

    // Delete user
    @Test
    void testDeleteUser_Success() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        userService.deleteUser(user.getId());

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.deleteUser(user.getId()));
    }

}
