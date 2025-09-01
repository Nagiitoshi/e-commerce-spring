package com.nagi.e_commerce_spring.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nagi.e_commerce_spring.exception.BusinessException;
import com.nagi.e_commerce_spring.exception.ResourceNotFoundException;
import com.nagi.e_commerce_spring.model.Category;
import com.nagi.e_commerce_spring.model.Product;
import com.nagi.e_commerce_spring.repository.CategoryRepository;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .id(1L)
                .name("Category1")
                .build();
    }

    // Create category
    @Test
    void testCreateCategory_Success() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category request = Category.builder()
                .name("Category1")
                .build();

        Category created = categoryService.createCategory(request);

        assertNotNull(created);
        assertEquals("Category1", created.getName());
    }

    // Update category
    @Test
    void testUpdateCategory_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category request = Category.builder()
                .name("Updated")
                .build();

        Category updated = categoryService.updateCategory(1L, request);

        assertEquals("Updated", updated.getName());
    }

    @Test
    void testUpdateCategory_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        Category request = Category.builder()
                .name("Updated")
                .build();

        assertThrows(ResourceNotFoundException.class,
                () -> categoryService.updateCategory(1L, request));
    }

    // Delete category
    @Test
    void testDeleteCategory_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).delete(category);

        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
    void testDeleteCategory_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> categoryService.deleteCategory(1L));
    }

    @Test
    void testDeleteCategory_HasProducts() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        category.setProducts(List.of(new Product())); // simula categoria com produtos

        assertThrows(BusinessException.class,
                () -> categoryService.deleteCategory(1L));
    }
}
