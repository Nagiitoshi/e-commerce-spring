package com.nagi.e_commerce_spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagi.e_commerce_spring.model.Address;
import com.nagi.e_commerce_spring.repository.AddressRepository;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    // List all addresses
    public List<Address> listAllAddresses() {
        return addressRepository.findAll();
    }

    // Create address
    public Address createAddress(Address address) {
        Address newAddress = Address.builder()
                .user(address.getUser())
                .cep(address.getCep())
                .street(address.getStreet())
                .number(address.getNumber())
                .neighborhood(address.getNeighborhood())
                .city(address.getCity())
                .state(address.getState())
                .build();

        return addressRepository.save(newAddress);
    }

    // Update address
    public Address updateAddress(Long id, Address addressDetails) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + id));

        address.setCep(addressDetails.getCep());
        address.setStreet(addressDetails.getStreet());
        address.setNumber(addressDetails.getNumber());
        address.setNeighborhood(addressDetails.getNeighborhood());
        address.setCity(addressDetails.getCity());
        address.setState(addressDetails.getState());

        return addressRepository.save(address);
    }

    // Delete address
    public void deleteAddress(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + id));
        addressRepository.delete(address);
    }
}
