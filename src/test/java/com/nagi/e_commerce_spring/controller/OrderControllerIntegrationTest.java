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
public class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testCreateOrder_AsUser_Success() throws Exception {
        String orderJson = """
            {
              "userId": 1,
              "items": [
                { "productId": 1, "quantity": 2 },
                { "productId": 2, "quantity": 1 }
              ]
            }
            """;

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.items").isArray());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetUserOrders_Success() throws Exception {
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetOrderById_Success() throws Exception {
        mockMvc.perform(get("/api/orders/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAllOrders_AsAdmin_Success() throws Exception {
        mockMvc.perform(get("/api/orders/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
    
}
