package com.nagi.e_commerce_spring.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void testCreateUser_AsAdmin_Success() throws Exception {
        String userJson = """
                {
                  "name": "John Doe",
                  "email": "john@example.com",
                  "password": "123456"
                }
                """;

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    @WithMockUser(username = "user", roles = { "USER" })
    void testCreateUser_AsUser_Forbidden() throws Exception {
        String userJson = """
                {
                  "name": "Jane Doe",
                  "email": "jane@example.com",
                  "password": "654321"
                }
                """;

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void testGetUserById_AsAdmin_Success() throws Exception {
        // supondo que existe usuário ID 1 no banco
        mockMvc.perform(get("/api/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username = "user", roles = { "USER" })
    void testGetUserById_AsUser_Success() throws Exception {
        // user pode acessar o próprio ID (dependendo da regra de negócio)
        mockMvc.perform(get("/api/users/{id}", 1L))
                .andExpect(status().isOk());
    }
}
