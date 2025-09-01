package com.nagi.e_commerce_spring.repository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.nagi.e_commerce_spring.model.Category;
import com.nagi.e_commerce_spring.model.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;
    private Product product;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .name("Eletrônicos")
                .build();
        categoryRepository.save(category);

        product = Product.builder()
                .name("Notebook")
                .description("Notebook gamer")
                .price(5000.0)
                .quantity(10)
                .category(category)
                .available(true)
                .build();
        productRepository.save(product);
    }

    @Test
    void testFindByNameContaining_Success() {

        Pageable pageable = PageRequest.of(0, 10); 
        Page<Product> productsPage = productRepository.findByNameContainingIgnoreCase("Notebook", pageable);

        List<Product> products = productsPage.getContent();
        assertFalse(products.isEmpty());
        assertEquals(product.getName(), products.get(0).getName());
        assertFalse(products.isEmpty());
        assertEquals(product.getName(), products.get(0).getName());
    }

    @Test
    void testFindByCategory_Success() {
        List<Product> products = productRepository.findByCategory(category);
        assertFalse(products.isEmpty());
        assertEquals(category.getId(), products.get(0).getCategory().getId());
    }
}
