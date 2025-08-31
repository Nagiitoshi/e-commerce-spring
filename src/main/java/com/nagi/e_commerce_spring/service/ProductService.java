package com.nagi.e_commerce_spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.nagi.e_commerce_spring.dto.product.ProductRequestDTO;
import com.nagi.e_commerce_spring.dto.product.ProductResponseDTO;
import com.nagi.e_commerce_spring.exception.BusinessException;
import com.nagi.e_commerce_spring.exception.ResourceNotFoundException;
import com.nagi.e_commerce_spring.exception.ValidationException;
import com.nagi.e_commerce_spring.model.Category;
import com.nagi.e_commerce_spring.model.Product;
import com.nagi.e_commerce_spring.repository.CategoryRepository;
import com.nagi.e_commerce_spring.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // List all products
    public Page<ProductResponseDTO> getAllProducts(String name, Category category, Pageable pageable) {
        Page<Product> products;

        if (name != null && category != null) {
            products = productRepository.findByNameContainingIgnoreCaseAndCategory(name, category, pageable);
        } else if (name != null) {
            products = productRepository.findByNameContainingIgnoreCase(name, pageable);
        } else if (category != null) {
            products = productRepository.findByCategory(category, pageable);
        } else {
            products = productRepository.findAll(pageable);
        }

        return products.map(this::toResponse);
    }

    // get product by id
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return toResponse(product);
    }

    // get products by category
    public List<ProductResponseDTO> getProductsByCategory(Category category) {
        return productRepository.findByCategory(category).stream()
                .map(this::toResponse)
                .toList();
    }

    // get by name
    public List<ProductResponseDTO> getProductsByName(String name) {
        return productRepository.findByNameContaining(name).stream()
                .map(this::toResponse)
                .toList();
    }

    // Create
    public ProductResponseDTO createProduct(ProductRequestDTO request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));

        Product newProduct = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .imageUrl(request.getImageUrl())
                .available(request.isAvailable())
                .category(category)
                .build();

        validatePriceAndQuantity(newProduct);

        return toResponse(productRepository.save(newProduct));
    }

    // Update
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setImageUrl(request.getImageUrl());
        product.setAvailable(request.isAvailable());
        product.setCategory(category);

        return toResponse(productRepository.save(product));
    }

    // Delete product
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    // Disable product (admin)
    public void deactivateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setAvailable(false);
        productRepository.save(product);
    }

    public void checkStockOrFail(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        if (product.getQuantity() < quantity) {
            throw new BusinessException("Insufficient stock for the product: " + product.getName());
        }
    }

    private ProductResponseDTO toResponse(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .imageUrl(product.getImageUrl())
                .available(product.isAvailable())
                .categoryName(product.getCategory().getName())
                .categoryId(product.getCategory().getId())
                .build();
    }

    private void validatePriceAndQuantity(Product product) {
        if (product.getPrice() <= 0) {
            throw new ValidationException("Price must be greater than zero.");
        }
        if (product.getQuantity() < 0) {
            throw new ValidationException("Stock cannot be negative.");
        }
    }
}
