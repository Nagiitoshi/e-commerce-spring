package com.nagi.e_commerce_spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nagi.e_commerce_spring.dto.order.OrderRequestDTO;
import com.nagi.e_commerce_spring.dto.order.OrderResponseDTO;
import com.nagi.e_commerce_spring.model.Order;
import com.nagi.e_commerce_spring.model.Users;
import com.nagi.e_commerce_spring.model.enums.OrderStatus;
import com.nagi.e_commerce_spring.model.enums.Role;
import com.nagi.e_commerce_spring.repository.UserRepository;
import com.nagi.e_commerce_spring.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(
            @PathVariable Long userId,
            @RequestBody OrderRequestDTO request) {

        OrderResponseDTO response = orderService.createOrder(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> listOrders(@PathVariable Long userId) {
        List<OrderResponseDTO> response = orderService.listOrders(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long orderId) {
        OrderResponseDTO response = orderService.getOrderById(orderId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/admin/{id}/status")
    public Order updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status,
            @RequestParam Long adminUserId) {
        Users adminUser = userRepository.findById(adminUserId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + adminUserId));

        orderService.validatePermissions(adminUser, Role.ADMIN); // validar permissão de admin
        return orderService.updateStatus(id, status);
    }
}
