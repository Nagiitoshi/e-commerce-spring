package com.nagi.e_commerce_spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nagi.e_commerce_spring.dto.cart.CartItemRequestDTO;
import com.nagi.e_commerce_spring.dto.cart.CartItemResponseDTO;
import com.nagi.e_commerce_spring.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public List<CartItemResponseDTO> listCartItems(@RequestParam Long userId) {
        return cartService.listItems(userId);
    }

    @PostMapping("/items")
    public CartItemResponseDTO addItemToCart(@RequestParam Long userId,
            @RequestBody CartItemRequestDTO request) {
        return cartService.addItem(userId, request);
    }

    @PutMapping("/items/{id}")
    public CartItemResponseDTO updateCartItem(@PathVariable Long id,
            @RequestBody CartItemRequestDTO request) {
        var cartItem = cartService.listItems(request.getProductId()).stream()
                .filter(ci -> ci.getProductId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + id));

        return cartService.addItem(cartItem.getProductId(), request);
    }

    @DeleteMapping("/items/{id}")
    public void removeItemFromCart(@PathVariable Long id, @RequestParam Long userId) {
        cartService.removeItem(userId, id);
    }
}
