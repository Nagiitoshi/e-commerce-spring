package com.nagi.e_commerce_spring.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagi.e_commerce_spring.dto.cart.CartItemResponseDTO;
import com.nagi.e_commerce_spring.dto.order.OrderRequestDTO;
import com.nagi.e_commerce_spring.dto.order.OrderResponseDTO;
import com.nagi.e_commerce_spring.model.CartItem;
import com.nagi.e_commerce_spring.model.Order;
import com.nagi.e_commerce_spring.model.Users;
import com.nagi.e_commerce_spring.model.enums.OrderStatus;
import com.nagi.e_commerce_spring.model.enums.Role;
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


    // Create order from user's cart

    public OrderResponseDTO createOrder(Long userId, OrderRequestDTO request) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Busca todos os itens do carrinho do usuário e filtra pelos IDs enviados
        List<CartItem> cartItems = cartItemRepository.findByUser_Id(userId).stream()
                .filter(item -> request.getItemsCartIds().contains(item.getId()))
                .toList();

        if (cartItems.isEmpty()) {
            throw new RuntimeException("No cart items found for the given IDs.");
        }

        double totalPrice = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.PENDING)
                .totalPrice(totalPrice)
                .createdIn(LocalDateTime.now())
                .build();

        cartItemRepository.deleteAll(cartItems);

        Order savedOrder = orderRepository.save(order);
        return toResponse(savedOrder, cartItems);
    }

    // List all orders for a user
    public List<OrderResponseDTO> listOrders(Long userId) {
        List<Order> orders = orderRepository.findByUser_IdOrderByCreatedInDesc(userId);

        return orders.stream()
                .map(order -> {
                    List<CartItem> items = cartItemRepository.findByUser_Id(userId);
                    return toResponse(order, items);
                })
                .toList();
    }

    // Find order by ID
    public OrderResponseDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        // Busca os cart items originais, não DTOs
        List<CartItem> cartItems = cartItemRepository.findByUser_Id(order.getUser().getId());

        return toResponse(order, cartItems);
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

    private OrderResponseDTO toResponse(Order order, List<CartItem> cartItems) {
        List<CartItemResponseDTO> itemsDTO = cartItems.stream()
                .map(this::cartItemToResponse)
                .toList();

        return OrderResponseDTO.builder()
                .id(order.getId())
                .data(order.getCreatedIn())
                .status(order.getStatus().name())
                .items(itemsDTO)
                .total(order.getTotalPrice())
                .build();
    }

    private CartItemResponseDTO cartItemToResponse(CartItem item) {
        return CartItemResponseDTO.builder()
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .price(item.getProduct().getPrice())
                .build();
    }
}
