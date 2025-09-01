package com.nagi.e_commerce_spring.repository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.nagi.e_commerce_spring.model.CartItem;
import com.nagi.e_commerce_spring.model.Product;

@DataJpaTest
public class CartItemRepositoryTest {

        @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testSaveAndFindById() {
        Product product = new Product();
        product.setName("Notebook Gamer");
        product.setDescription("16GB RAM, SSD 512GB");
        product.setPrice(4500.00);
        product.setQuantity(5);
        Product savedProduct = productRepository.save(product);

        CartItem cartItem = new CartItem();
        cartItem.setProduct(savedProduct);
        cartItem.setQuantity(2);

        CartItem savedCartItem = cartItemRepository.save(cartItem);

        Optional<CartItem> found = cartItemRepository.findById(savedCartItem.getId());

        assertTrue(found.isPresent());
        assertEquals(2, found.get().getQuantity());
        assertEquals("Notebook Gamer", found.get().getProduct().getName());
    }
}
