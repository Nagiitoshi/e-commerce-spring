package com.nagi.e_commerce_spring.repository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.nagi.e_commerce_spring.model.Users;
import com.nagi.e_commerce_spring.model.enums.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private Users users;

    @BeforeEach
    void setUp() {
        users = Users.builder()
                .username("nagi")
                .email("nagi@email.com")
                .password("12345678")
                .role(Role.USER)
                .phoneNumber("123456789")
                .build();
        userRepository.save(users);
    }

    @Test
    void testFindByEmail_Success() {
        Optional<Users> found = userRepository.findByEmail("nagi@email.com");
        assertTrue(found.isPresent());
        assertEquals(users.getEmail(), found.get().getEmail());
    }

    @Test
    void testFindByEmail_NotFound() {
        Optional<Users> found = userRepository.findByEmail("notfound@email.com");
        assertFalse(found.isPresent());
    }

    @Test
    void testExistsByEmail_Success() {
        boolean exists = userRepository.existsByEmail("nagi@email.com");
        assertTrue(exists);
    }

    @Test
    void testExistsByEmail_False() {
        boolean exists = userRepository.existsByEmail("notfound@email.com");
        assertFalse(exists);
    }

}
