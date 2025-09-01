package com.nagi.e_commerce_spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nagi.e_commerce_spring.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

        List<Order> findByUser_IdOrderByCreatedInDesc(Long userId);
}
