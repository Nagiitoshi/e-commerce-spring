package com.nagi.e_commerce_spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagi.e_commerce_spring.model.Order;
import com.nagi.e_commerce_spring.model.Users;
import com.nagi.e_commerce_spring.model.enums.OrderStatus;
import com.nagi.e_commerce_spring.model.enums.Role;
import com.nagi.e_commerce_spring.repository.AddressRepository;
import com.nagi.e_commerce_spring.repository.CartItemRepository;
import com.nagi.e_commerce_spring.repository.OrderRepository;
import com.nagi.e_commerce_spring.repository.UserRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private AddressRepository addressRepository;

    // Create order from user's cart

    public Order createOrder(Long userId, Long addressId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        var address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + addressId));

        var cartItems = cartItemRepository.findByUser_Id(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty, cannot create order.");
        }

        double totalPrice = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        Order order = Order.builder()
                .user(user)
                .addressId(address)
                .status(OrderStatus.PENDING)
                .totalPrice(totalPrice)
                .build();

        cartItemRepository.deleteAll(cartItems);

        return orderRepository.save(order);
    }

    // List all orders for a user
    public List<Order> listOrders(Long userId) {
        return orderRepository.findByUser_IdOrderByCreatedInDesc(userId);
    }

    // Find order by ID
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
    }

    // Update order status (admin)
    public Order updateStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    public void validatePermissions(Users user, Role requiredRole) {
        if (user.getRole() != requiredRole) {
            throw new RuntimeException("Permission denied.");
        }
    }
}
