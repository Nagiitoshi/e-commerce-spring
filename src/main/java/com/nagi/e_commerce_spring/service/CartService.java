package com.nagi.e_commerce_spring.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public CartItem addItem(Long userId, Long productId, int quantity) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found."));

        validateStockBeforeAddingToCart(product, quantity);

        if (!product.isAvailable()) {
            throw new RuntimeException("Product is not available");
        }

        CartItem cartItem = CartItem.builder()
                .user(user)
                .product(product)
                .quantity(quantity)
                .addedIn(LocalDateTime.now())
                .build();

        return cartItemRepository.save(cartItem);
    }

    // Remove item from cart
    public void removeItem(Long userId, Long productId) {
        cartItemRepository.deleteByUser_IdAndProduct_Id(userId, productId);
    }

    // List all items in user's cart
    public List<CartItem> listItems(Long userId) {
        return cartItemRepository.findByUser_Id(userId);
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
            throw new RuntimeException("Not enough stock available.");
        }
    }
}
