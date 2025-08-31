package com.nagi.e_commerce_spring.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nagi.e_commerce_spring.model.Category;
import com.nagi.e_commerce_spring.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Product> findByCategory(Category category, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCaseAndCategory(String name, Category category, Pageable pageable);

    List<Product> findByCategory(Category category);

    List<Product> findByNameContaining(String name);
}