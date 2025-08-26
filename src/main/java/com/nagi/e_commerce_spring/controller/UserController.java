package com.nagi.e_commerce_spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nagi.e_commerce_spring.dto.address.AddressRequestDTO;
import com.nagi.e_commerce_spring.dto.address.AddressResponseDTO;
import com.nagi.e_commerce_spring.dto.user.UserRequestDTO;
import com.nagi.e_commerce_spring.dto.user.UserResponseDTO;
import com.nagi.e_commerce_spring.model.Address;
import com.nagi.e_commerce_spring.model.Users;
import com.nagi.e_commerce_spring.model.enums.Role;
import com.nagi.e_commerce_spring.repository.AddressRepository;
import com.nagi.e_commerce_spring.repository.UserRepository;
import com.nagi.e_commerce_spring.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserResponseDTO> getProfile(@PathVariable Long userId, Authentication authentication) {
        String username = authentication.getName();
        Users authUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!authUser.getId().equals(userId) && authUser.getRole() != Role.ADMIN) {
            throw new RuntimeException("Permission denied.");
        }

        UserResponseDTO response = new UserResponseDTO(
                authUser.getId(),
                authUser.getUsername(),
                authUser.getEmail(),
                authUser.getPhoneNumber(),
                authUser.getRole().name());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile/{userId}")
    public ResponseEntity<UserResponseDTO> updateProfile(
            @PathVariable Long userId,
            @RequestBody UserRequestDTO updateRequest,
            Authentication authentication) {

        String username = authentication.getName();
        Users authUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!authUser.getId().equals(userId) && authUser.getRole() != Role.ADMIN) {
            throw new RuntimeException("Permission denied.");
        }

        UserResponseDTO response = userService.updateProfile(userId, updateRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/addresses")
    public ResponseEntity<List<AddressResponseDTO>> getAddress(@PathVariable Long userId) {
        List<Address> addresses = addressRepository.findByUser_Id(userId);

        List<AddressResponseDTO> response = addresses.stream()
                .map(addr -> new AddressResponseDTO(
                        addr.getId(),
                        addr.getStreet(),
                        addr.getCity(),
                        addr.getState(),
                        addr.getCep()))
                .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{userId}/addresses")
    public ResponseEntity<AddressResponseDTO> addAddress(
            @PathVariable Long userId,
            @RequestBody AddressRequestDTO request) {

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Address address = new Address();
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCep(request.getCep());
        address.setUser(user);

        Address saved = addressRepository.save(address);

        AddressResponseDTO response = new AddressResponseDTO(
                saved.getId(),
                saved.getStreet(),
                saved.getCity(),
                saved.getState(),
                saved.getCep());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}/addresses/{addressId}")
    public ResponseEntity<AddressResponseDTO> updateAddress(
            @PathVariable Long userId,
            @PathVariable Long addressId,
            @RequestBody AddressRequestDTO request) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + addressId));

        if (!address.getUser().getId().equals(userId)) {
            throw new RuntimeException("Permission denied.");
        }

        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCep(request.getCep());

        Address updated = addressRepository.save(address);

        AddressResponseDTO response = new AddressResponseDTO(
                updated.getId(),
                updated.getStreet(),
                updated.getCity(),
                updated.getState(),
                updated.getCep());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}/addresses/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long userId, @PathVariable Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + addressId));

        if (!address.getUser().getId().equals(userId)) {
            throw new RuntimeException("Permission denied.");
        }

        addressRepository.delete(address);

        return ResponseEntity.noContent().build();
    }
}
