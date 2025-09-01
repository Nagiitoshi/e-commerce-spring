package com.nagi.e_commerce_spring.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nagi.e_commerce_spring.dto.order.OrderRequestDTO;
import com.nagi.e_commerce_spring.dto.order.OrderResponseDTO;
import com.nagi.e_commerce_spring.exception.BusinessException;
import com.nagi.e_commerce_spring.exception.ResourceNotFoundException;
import com.nagi.e_commerce_spring.model.CartItem;
import com.nagi.e_commerce_spring.model.Order;
import com.nagi.e_commerce_spring.model.Product;
import com.nagi.e_commerce_spring.model.Users;
import com.nagi.e_commerce_spring.model.enums.OrderStatus;
import com.nagi.e_commerce_spring.repository.CartItemRepository;
import com.nagi.e_commerce_spring.repository.OrderRepository;
import com.nagi.e_commerce_spring.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private UserRepository userRepository;

    private Users user;
    private CartItem cartItem;
    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = Users.builder()
                .id(1L)
                .username("nagi")
                .email("nagi@email.com")
                .build();

        cartItem = CartItem.builder()
                .id(1L)
                .user(user)
                .quantity(2)
                .product(Product.builder().id(1L).name("Product1").price(100.0).build())
                .build();

        order = Order.builder()
                .id(1L)
                .user(user)
                .status(OrderStatus.PENDING)
                .totalPrice(200.0)
                .build();
    }

    @Test
    void testCreateOrder_Success() {
        OrderRequestDTO request = OrderRequestDTO.builder()
                .itemsCartIds(List.of(1L))
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartItemRepository.findByUser_Id(1L)).thenReturn(List.of(cartItem));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderResponseDTO response = orderService.createOrder(1L, request);

        assertNotNull(response);
        assertEquals(OrderStatus.PENDING.name(), response.getStatus());
        assertEquals(200.0, response.getTotal());
    }

    @Test
    void testCreateOrder_NoCartItems() {
        OrderRequestDTO request = OrderRequestDTO.builder()
                .itemsCartIds(List.of(1L))
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartItemRepository.findByUser_Id(1L)).thenReturn(Collections.emptyList());

        assertThrows(BusinessException.class, () -> orderService.createOrder(1L, request));
    }

    @Test
    void testGetOrderById_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(cartItemRepository.findByUser_Id(user.getId())).thenReturn(List.of(cartItem));

        OrderResponseDTO response = orderService.getOrderById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void testGetOrderById_NotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(1L));
    }
    
}
