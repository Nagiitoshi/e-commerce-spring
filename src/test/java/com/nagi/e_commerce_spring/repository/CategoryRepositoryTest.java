package com.nagi.e_commerce_spring.repository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.nagi.e_commerce_spring.model.Category;

@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testSaveAndFindById() {
        Category category = new Category();
        category.setName("Eletrônicos");

        Category savedCategory = categoryRepository.save(category);

        Optional<Category> found = categoryRepository.findById(savedCategory.getId());

        assertTrue(found.isPresent());
        assertEquals("Eletrônicos", found.get().getName());
    }
}
