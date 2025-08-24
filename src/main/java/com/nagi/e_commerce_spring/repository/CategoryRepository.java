package com.nagi.e_commerce_spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nagi.e_commerce_spring.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

    List<Category> findByNameContaining(String name);
}
