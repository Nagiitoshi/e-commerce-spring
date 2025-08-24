package com.nagi.e_commerce_spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nagi.e_commerce_spring.model.CartItem;
import com.nagi.e_commerce_spring.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public List<CartItem> listCartItems(@RequestParam Long userId) {
        return cartService.listItems(userId);
    }

    @PostMapping("/items")
    public CartItem addItemToCart(@RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        return cartService.addItem(userId, productId, quantity);
    }

    @PutMapping("/items/{id}")
    public CartItem updateCartItem(@PathVariable Long id, @RequestParam int quantity) {
        CartItem item = cartService.listItems(id).stream()
                .filter(ci -> ci.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + id));
        item.setQuantity(quantity);
        return cartService.addItem(item.getUser().getId(), item.getProduct().getId(), quantity);
    }

    @DeleteMapping("/items/{id}")
    public void removeItemFromCart(@PathVariable Long id, @RequestParam Long userId) {
        CartItem item = cartService.listItems(userId).stream()
                .filter(ci -> ci.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + id));
        cartService.removeItem(userId, item.getProduct().getId());
    }
}
