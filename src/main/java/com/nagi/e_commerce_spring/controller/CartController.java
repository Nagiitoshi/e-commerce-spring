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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Operation(summary = "Listar itens do carrinho", description = "Lista todos os itens do carrinho de um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Itens do carrinho retornados com sucesso")
    })
    @GetMapping
    public List<CartItemResponseDTO> listCartItems(@RequestParam Long userId) {
        return cartService.listItems(userId);
    }

    @Operation(summary = "Adicionar item ao carrinho", description = "Adiciona um item ao carrinho de um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item adicionado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário ou produto não encontrado")
    })
    @PostMapping("/items")
    public CartItemResponseDTO addItemToCart(@RequestParam Long userId,
            @RequestBody CartItemRequestDTO request) {
        return cartService.addItem(userId, request);
    }

    @Operation(summary = "Atualizar item do carrinho", description = "Atualiza a quantidade ou detalhes de um item no carrinho")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Item do carrinho não encontrado")
    })
    @PutMapping("/items/{id}")
    public CartItemResponseDTO updateCartItem(@PathVariable Long id,
            @RequestBody CartItemRequestDTO request) {
        var cartItem = cartService.listItems(request.getProductId()).stream()
                .filter(ci -> ci.getProductId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + id));

        return cartService.addItem(cartItem.getProductId(), request);
    }

    @Operation(summary = "Remover item do carrinho", description = "Remove um item do carrinho de um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Item do carrinho não encontrado")
    })
    @DeleteMapping("/items/{id}")
    public void removeItemFromCart(@PathVariable Long id, @RequestParam Long userId) {
        cartService.removeItem(userId, id);
    }
}
