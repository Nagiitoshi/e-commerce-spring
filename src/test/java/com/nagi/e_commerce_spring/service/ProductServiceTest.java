package com.nagi.e_commerce_spring.service;

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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nagi.e_commerce_spring.dto.product.ProductRequestDTO;
import com.nagi.e_commerce_spring.dto.product.ProductResponseDTO;
import com.nagi.e_commerce_spring.exception.BusinessException;
import com.nagi.e_commerce_spring.exception.ResourceNotFoundException;
import com.nagi.e_commerce_spring.model.Category;
import com.nagi.e_commerce_spring.model.Product;
import com.nagi.e_commerce_spring.repository.CategoryRepository;
import com.nagi.e_commerce_spring.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        category = Category.builder().id(1L).name("Category1").build();
        product = Product.builder()
                .id(1L)
                .name("Product1")
                .description("Desc")
                .price(100.0)
                .quantity(10)
                .category(category)
                .available(true)
                .build();
    }

    // GET product by ID
    @Test
    void testGetProductById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponseDTO found = productService.getProductById(1L);

        assertNotNull(found);
        assertEquals(product.getId(), found.getId());
        assertEquals("Product1", found.getName());
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.getProductById(1L));
    }

    // CREATE product
    @Test
    void testCreateProduct_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductRequestDTO request = ProductRequestDTO.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .categoryId(product.getCategory().getId())
                .available(product.isAvailable())
                .build();

        ProductResponseDTO created = productService.createProduct(request);

        assertNotNull(created);
        assertEquals("Product1", created.getName());
        assertEquals(100.0, created.getPrice());
    }

    @Test
    void testCreateProduct_InvalidPrice() {
        ProductRequestDTO request = ProductRequestDTO.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(0.0) // preço inválido
                .quantity(product.getQuantity())
                .categoryId(product.getCategory().getId())
                .available(product.isAvailable())
                .build();

        assertThrows(BusinessException.class,
                () -> productService.createProduct(request));
    }

    // UPDATE product
    @Test
    void testUpdateProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductRequestDTO request = ProductRequestDTO.builder()
                .name("Updated")
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .categoryId(product.getCategory().getId())
                .available(product.isAvailable())
                .build();

        ProductResponseDTO updated = productService.updateProduct(1L, request);

        assertNotNull(updated);
        assertEquals("Updated", updated.getName());
    }

    @Test
    void testUpdateProduct_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ProductRequestDTO request = ProductRequestDTO.builder()
                .name("Updated")
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .categoryId(product.getCategory().getId())
                .available(product.isAvailable())
                .build();

        assertThrows(ResourceNotFoundException.class,
                () -> productService.updateProduct(1L, request));
    }

    // DELETE product
    @Test
    void testDeleteProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(product);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void testDeleteProduct_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.deleteProduct(1L));
    }
}