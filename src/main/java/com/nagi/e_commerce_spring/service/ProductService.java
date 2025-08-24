package com.nagi.e_commerce_spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.nagi.e_commerce_spring.model.Category;
import com.nagi.e_commerce_spring.model.Product;
import com.nagi.e_commerce_spring.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // List all products
    public Page<Product> getAllProducts(String name, Category category, Pageable pageable) {
        if (name != null && category != null) {
            return productRepository.findByNameContainingIgnoreCaseAndCategory(name, category, pageable);
        } else if (name != null) {
            return productRepository.findByNameContainingIgnoreCase(name, pageable);
        } else if (category != null) {
            return productRepository.findByCategory(category, pageable);
        } else {
            return productRepository.findAll(pageable);
        }
    }

    // get product by id
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    // get products by category
    public List<Product> getProductsByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    // get by name
    public List<Product> getProductsByName(String name) {
        return productRepository.findByNameContaining(name);
    }

    public Product createProduct(Product product) {
        Product newProduct = Product.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .imageUrl(product.getImageUrl())
                .available(product.isAvailable())
                .category(product.getCategory())
                .build();

        validatePriceAndQuantity(newProduct);

        return productRepository.save(newProduct);
    }

    // Update product
    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setQuantity(productDetails.getQuantity());
        product.setImageUrl(productDetails.getImageUrl());
        product.setAvailable(productDetails.isAvailable());
        product.setCategory(productDetails.getCategory());

        return productRepository.save(product);
    }

    // Delete product
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    // Disable product (admin)
    public void deactivateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        product.setAvailable(false);
        productRepository.save(product);
    }

    public void checkStockOrFail(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        if (product.getQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient stock for the product: " + product.getName());
        }
    }

    private void validatePriceAndQuantity(Product product) {
        if (product.getPrice().doubleValue() <= 0) {
            throw new RuntimeException("Price must be greater than zero.");
        }
        if (product.getQuantity() < 0) {
            throw new RuntimeException("Stock cannot be negative.");
        }
    }
}
