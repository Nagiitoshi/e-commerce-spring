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
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void testCreateProduct_AsAdmin_Success() throws Exception {
        String productJson = """
                {
                  "name": "Notebook Dell",
                  "description": "Notebook i7 16GB RAM",
                  "price": 4500.00
                }
                """;

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Notebook Dell"));
    }

    @Test
    @WithMockUser(username = "user", roles = { "USER" })
    void testCreateProduct_AsUser_Forbidden() throws Exception {
        String productJson = """
                {
                  "name": "Smartphone Samsung",
                  "description": "Galaxy S23",
                  "price": 3500.00
                }
                """;

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", roles = { "USER" })
    void testGetProductById_Success() throws Exception {
        mockMvc.perform(get("/api/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username = "user", roles = { "USER" })
    void testGetAllProducts_Success() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
