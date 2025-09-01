package com.nagi.e_commerce_spring.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagi.e_commerce_spring.model.CartItem;
import com.nagi.e_commerce_spring.model.Product;
import com.nagi.e_commerce_spring.model.Users;
import com.nagi.e_commerce_spring.repository.CartItemRepository;
import com.nagi.e_commerce_spring.repository.ProductRepository;
import com.nagi.e_commerce_spring.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerIntegrationTest {

        @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Users user;
    private Product product;

    @BeforeEach
    void setup() {
        cartRepository.deleteAll();
        userRepository.deleteAll();
        productRepository.deleteAll();

        user = new Users();
        user.setUsername("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("123456");
        user = userRepository.save(user);

        product = new Product();
        product.setName("Notebook Gamer");
        product.setDescription("Um notebook potente para jogos");
        product.setPrice(4500.0);
        product.setQuantity(10);
        product = productRepository.save(product);
    }

    @Test
    void shouldAddItemToCart() throws Exception {
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        mockMvc.perform(post("/api/carts/{userId}/items", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].product.name").value("Notebook Gamer"))
                .andExpect(jsonPath("$.items[0].quantity").value(2));
    }

    @Test
    void shouldGetCartByUserId() throws Exception {
        CartItem cart = new CartItem();
        cart.setUser(user);
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(1);
        cartRepository.save(cart);

        mockMvc.perform(get("/api/carts/{userId}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].product.name").value("Notebook Gamer"))
                .andExpect(jsonPath("$.items[0].quantity").value(1));
    }

    @Test
    void shouldRemoveItemFromCart() throws Exception {
        CartItem cart = new CartItem();
        cart.setUser(user);
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(1);
        
        mockMvc.perform(delete("/api/carts/{userId}/items/{itemId}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isEmpty());
    }
}
