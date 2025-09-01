package com.nagi.e_commerce_spring.repository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.nagi.e_commerce_spring.model.Address;

@DataJpaTest
public class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    @Test
    void testSaveAndFindById() {
        Address address = new Address();
        address.setStreet("Rua Teste");
        address.setCity("Maringá");
        address.setState("PR");
        address.setCep("87000-000");

        Address savedAddress = addressRepository.save(address);

        Optional<Address> found = addressRepository.findById(savedAddress.getId());

        assertTrue(found.isPresent());
        assertEquals("Rua Teste", found.get().getStreet());
        assertEquals("Maringá", found.get().getCity());
    }
}
