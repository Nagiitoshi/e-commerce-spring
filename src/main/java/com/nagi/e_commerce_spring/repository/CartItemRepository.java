package com.nagi.e_commerce_spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nagi.e_commerce_spring.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Object> {

    List<CartItem> findByUser_Id(Long userId);

    void deleteByUser_IdAndProduct_Id(Long userId, Long productId);
}
