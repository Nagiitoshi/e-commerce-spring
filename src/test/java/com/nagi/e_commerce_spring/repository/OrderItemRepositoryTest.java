package com.nagi.e_commerce_spring.repository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.nagi.e_commerce_spring.model.OrderItem;
import com.nagi.e_commerce_spring.model.Product;

@DataJpaTest
public class OrderItemRepositoryTest {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testSaveAndFindById() {
        Product product = new Product();
        product.setName("Smartphone");
        product.setDescription("128GB, 6GB RAM");
        product.setPrice(2500.00);
        product.setQuantity(10);
        Product savedProduct = productRepository.save(product);

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(savedProduct);
        orderItem.setQuantity(3);
        orderItem.setUnitPrice(BigDecimal.valueOf(savedProduct.getPrice()));

        OrderItem savedOrderItem = orderItemRepository.save(orderItem);

        Optional<OrderItem> found = orderItemRepository.findById(savedOrderItem.getId());

        assertTrue(found.isPresent());
        assertEquals(3, found.get().getQuantity());
        assertEquals(BigDecimal.valueOf(2500.00), found.get().getUnitPrice());
        assertEquals("Smartphone", found.get().getProduct().getName());
    }
}
