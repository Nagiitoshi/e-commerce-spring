package com.nagi.e_commerce_spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.nagi.e_commerce_spring.model.Address;
import com.nagi.e_commerce_spring.model.Users;
import com.nagi.e_commerce_spring.model.enums.Role;
import com.nagi.e_commerce_spring.repository.AddressRepository;
import com.nagi.e_commerce_spring.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    @GetMapping("/profile/{userId}")
    public ResponseEntity<Users> getProfile(@PathVariable Long userId, Authentication authentication) {
        String username = authentication.getName();
        Users authUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!authUser.getId().equals(userId) && authUser.getRole() != Role.ADMIN) {
            throw new RuntimeException("Permission denied.");
        }

        return ResponseEntity.ok(authUser);
    }

    @PutMapping("/profile/{userId}")
    public ResponseEntity<Users> updateProfile(@PathVariable Long userId, @RequestBody Users updateUser) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        user.setUsername(updateUser.getUsername());
        user.setPhoneNumber(updateUser.getPhoneNumber());
        user.setPassword(updateUser.getPassword());

        return ResponseEntity.ok(userRepository.save(user));
    }

    @GetMapping("/{userId}/addresses")
    public ResponseEntity<List<Address>> getAddress(@PathVariable Long userid) {
        List<Address> addresses = addressRepository.findByUser_Id(userid);
        return ResponseEntity.ok(addresses);
    }

    @PostMapping("/{userId}/addresses")
    public ResponseEntity<Address> addAddress(@PathVariable Long userId, @RequestBody Address address) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        address.setUser(user);
        Address saved = addressRepository.save(address);

        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{userId}/addresses/{addressId}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long userId, @PathVariable Long addressId,
            @RequestBody Address updateAddress) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + addressId));

        if (!address.getUser().getId().equals(userId)) {
            throw new RuntimeException("Permission denied.");
        }

        address.setStreet(updateAddress.getStreet());
        address.setCity(updateAddress.getCity());
        address.setCep(updateAddress.getCep());

        return ResponseEntity.ok(addressRepository.save(address));
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

    @PostMapping
    public ResponseEntity<Users> createUser(@RequestBody Users user) {
        Users savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
}
