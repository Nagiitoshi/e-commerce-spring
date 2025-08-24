package com.nagi.e_commerce_spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nagi.e_commerce_spring.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{
    
}
