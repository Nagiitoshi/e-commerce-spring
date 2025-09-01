package com.nagi.e_commerce_spring.service;

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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nagi.e_commerce_spring.dto.cart.CartItemRequestDTO;
import com.nagi.e_commerce_spring.dto.cart.CartItemResponseDTO;
import com.nagi.e_commerce_spring.exception.ResourceNotFoundException;
import com.nagi.e_commerce_spring.model.CartItem;
import com.nagi.e_commerce_spring.model.Product;
import com.nagi.e_commerce_spring.model.Users;
import com.nagi.e_commerce_spring.repository.CartItemRepository;
import com.nagi.e_commerce_spring.repository.ProductRepository;
import com.nagi.e_commerce_spring.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    private Users user;
    private Product product;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = Users.builder()
                .id(1L)
                .username("nagi")
                .email("nagi@email.com")
                .build();

        product = Product.builder()
                .id(1L)
                .name("Product1")
                .price(100.0)
                .quantity(10)
                .available(true)
                .build();

        cartItem = CartItem.builder()
                .id(1L)
                .user(user)
                .product(product)
                .quantity(2)
                .build();
    }

    @Test
    void testAddItem_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        // Cria o DTO
        CartItemRequestDTO request = CartItemRequestDTO.builder()
                .productId(1L)
                .quantity(2)
                .build();

        CartItemResponseDTO response = cartService.addItem(1L, request);

        assertNotNull(response);
        assertEquals("Product1", response.getProductName());
        assertEquals(2, response.getQuantity());
    }

    @Test
    void testAddItem_ProductNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        CartItemRequestDTO request = CartItemRequestDTO.builder()
                .productId(1L)
                .quantity(2)
                .build();

        assertThrows(ResourceNotFoundException.class, () -> cartService.addItem(1L, request));
    }

    @Test
    void testListItems() {
        when(cartItemRepository.findByUser_Id(1L)).thenReturn(List.of(cartItem));

        List<CartItemResponseDTO> response = cartService.listItems(1L);

        assertEquals(1, response.size());
        assertEquals("Product1", response.get(0).getProductName());
    }

    @Test
    void testRemoveItem() {
        doNothing().when(cartItemRepository).deleteByUser_IdAndProduct_Id(1L, 1L);

        cartService.removeItem(1L, 1L);

        verify(cartItemRepository, times(1)).deleteByUser_IdAndProduct_Id(1L, 1L);
    }

    @Test
    void testCalculateTotal() {
        when(cartItemRepository.findByUser_Id(1L)).thenReturn(List.of(cartItem));

        double total = cartService.calculateTotal(1L);

        assertEquals(200.0, total);
    }

}
