package com.nagi.e_commerce_spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagi.e_commerce_spring.dto.address.AddressRequestDTO;
import com.nagi.e_commerce_spring.dto.address.AddressResponseDTO;
import com.nagi.e_commerce_spring.exception.ResourceNotFoundException;
import com.nagi.e_commerce_spring.model.Address;
import com.nagi.e_commerce_spring.repository.AddressRepository;
import com.nagi.e_commerce_spring.repository.UserRepository;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    // List all addresses
    public List<AddressResponseDTO> listAllAddresses() {
        return addressRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Create address
    public AddressResponseDTO createAddress(AddressRequestDTO request) {
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        Address newAddress = Address.builder()
                .user(user)
                .cep(request.getCep())
                .street(request.getStreet())
                .number(request.getNumber())
                .neighborhood(request.getNeighborhood())
                .city(request.getCity())
                .state(request.getState())
                .build();

        Address saved = addressRepository.save(newAddress);
        return toResponse(saved);
    }

    // Update address
    public AddressResponseDTO updateAddress(Long id, AddressRequestDTO request) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));

        address.setCep(request.getCep());
        address.setStreet(request.getStreet());
        address.setNumber(request.getNumber());
        address.setNeighborhood(request.getNeighborhood());
        address.setCity(request.getCity());
        address.setState(request.getState());

        Address updated = addressRepository.save(address);
        return toResponse(updated);
    }

    // Delete address
    public void deleteAddress(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));
        addressRepository.delete(address);
    }

    // Mapper
    private AddressResponseDTO toResponse(Address address) {
        return AddressResponseDTO.builder()
                .id(address.getId())
                .userId(address.getUser().getId())
                .cep(address.getCep())
                .street(address.getStreet())
                .number(address.getNumber())
                .neighborhood(address.getNeighborhood())
                .city(address.getCity())
                .state(address.getState())
                .build();
    }
}
