package com.nagi.e_commerce_spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagi.e_commerce_spring.exception.BusinessException;
import com.nagi.e_commerce_spring.exception.ResourceNotFoundException;
import com.nagi.e_commerce_spring.model.Category;
import com.nagi.e_commerce_spring.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // List all categories
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Create new category
    public Category createCategory(Category category) {
        Category newCategory = Category.builder()
                .name(category.getName())
                .description(category.getDescription())
                .build();

        return categoryRepository.save(newCategory);
    }

    // Update existing category
    public Category updateCategory(Long id, Category categoryDetails) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        category.setName(categoryDetails.getName());
        category.setDescription(categoryDetails.getDescription());

        return categoryRepository.save(category);
    }

    // Delete category (only if empty)
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        if (!category.getProducts().isEmpty()) {
            throw new BusinessException("Unable to delete category with linked products!!");
        }

        categoryRepository.delete(category);
    }
}
