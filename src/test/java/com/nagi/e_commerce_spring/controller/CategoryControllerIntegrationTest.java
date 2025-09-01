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
public class CategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void testCreateCategory_AsAdmin_Success() throws Exception {
        String categoryJson = """
                {
                  "name": "Eletrônicos"
                }
                """;

        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(categoryJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Eletrônicos"));
    }

    @Test
    @WithMockUser(username = "user", roles = { "USER" })
    void testCreateCategory_AsUser_Forbidden() throws Exception {
        String categoryJson = """
                {
                  "name": "Roupas"
                }
                """;

        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(categoryJson))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", roles = { "USER" })
    void testGetCategoryById_Success() throws Exception {
        mockMvc.perform(get("/api/categories/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username = "user", roles = { "USER" })
    void testGetAllCategories_Success() throws Exception {
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
