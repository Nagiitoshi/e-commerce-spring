package com.nagi.e_commerce_spring.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagi.e_commerce_spring.dto.cart.CartItemRequestDTO;
import com.nagi.e_commerce_spring.dto.cart.CartItemResponseDTO;
import com.nagi.e_commerce_spring.exception.BusinessException;
import com.nagi.e_commerce_spring.exception.ResourceNotFoundException;
import com.nagi.e_commerce_spring.model.CartItem;
import com.nagi.e_commerce_spring.model.Product;
import com.nagi.e_commerce_spring.model.Users;
import com.nagi.e_commerce_spring.repository.CartItemRepository;
import com.nagi.e_commerce_spring.repository.ProductRepository;
import com.nagi.e_commerce_spring.repository.UserRepository;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // Add item to cart
    public CartItemResponseDTO addItem(Long userId, CartItemRequestDTO request) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));

        if (!product.isAvailable()) {
            throw new BusinessException("Product is not available");
        }

        validateStockBeforeAddingToCart(product, request.getQuantity());

        CartItem cartItem = CartItem.builder()
                .user(user)
                .product(product)
                .quantity(request.getQuantity())
                .addedIn(LocalDateTime.now())
                .build();

        cartItemRepository.save(cartItem);

        return cartItemToResponse(cartItem);
    }

    // Remove item from cart
    public void removeItem(Long userId, Long productId) {
        cartItemRepository.deleteByUser_IdAndProduct_Id(userId, productId);
    }

    // List all items in user's cart
    public List<CartItemResponseDTO> listItems(Long userId) {
        return cartItemRepository.findByUser_Id(userId)
                .stream()
                .map(this::cartItemToResponse)
                .toList();
    }

    // Clear the cart for a user
    public void clearCart(Long userId) {
        var items = cartItemRepository.findByUser_Id(userId);
        cartItemRepository.deleteAll(items);
    }

    // Calculate total price of cart
    public double calculateTotal(Long userId) {
        var items = cartItemRepository.findByUser_Id(userId);
        return items.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    private void validateStockBeforeAddingToCart(Product product, int quantity) {
        if (product.getQuantity() < quantity) {
            throw new BusinessException("Not enough stock available.");
        }
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
