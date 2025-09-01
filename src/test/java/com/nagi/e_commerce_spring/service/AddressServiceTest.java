package com.nagi.e_commerce_spring.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.mockito.junit.jupiter.MockitoExtension;

import com.nagi.e_commerce_spring.dto.address.AddressRequestDTO;
import com.nagi.e_commerce_spring.dto.address.AddressResponseDTO;
import com.nagi.e_commerce_spring.exception.ResourceNotFoundException;
import com.nagi.e_commerce_spring.model.Address;
import com.nagi.e_commerce_spring.model.Users;
import com.nagi.e_commerce_spring.repository.AddressRepository;
import com.nagi.e_commerce_spring.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AddressService addressService;

    private Users user;

    private Address address;

    @BeforeEach
    void setUp() {
        user = Users.builder()
                .id(1L)
                .username("Nagi")
                .email("nagi@email.com")
                .build();

        address = Address.builder()
                .id(1L)
                .user(user)
                .street("Rua A")
                .city("Maringá")
                .state("PR")
                .cep("87000-000")
                .build();
    }

    // Create address
    @Test
    void testCreateAddress_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        AddressRequestDTO request = AddressRequestDTO.builder()
                .street("Rua A")
                .city("Maringá")
                .state("PR")
                .cep("87000-000")
                .build();

        AddressResponseDTO created = addressService.createAddress(request);

        assertNotNull(created);
        assertEquals("Rua A", created.getStreet());
    }

    // Update address
    @Test
    void testUpdateAddress_Success() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        AddressRequestDTO request = AddressRequestDTO.builder()
                .street("Rua B")
                .city("Maringá")
                .state("PR")
                .cep("87000-001")
                .build();

        AddressResponseDTO updated = addressService.updateAddress(1L, request);

        assertEquals("Rua B", updated.getStreet());
    }

    @Test
    void testUpdateAddress_NotFound() {
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        AddressRequestDTO request = AddressRequestDTO.builder()
                .street("Rua B")
                .city("Maringá")
                .state("PR")
                .cep("87000-001")
                .build();

        assertThrows(ResourceNotFoundException.class,
                () -> addressService.updateAddress(1L, request));
    }

    // Delete address
    @Test
    void testDeleteAddress_Success() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        doNothing().when(addressRepository).delete(address);

        addressService.deleteAddress(1L);

        verify(addressRepository, times(1)).delete(address);
    }

    @Test
    void testDeleteAddress_NotFound() {
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> addressService.deleteAddress(1L));
    }

}
