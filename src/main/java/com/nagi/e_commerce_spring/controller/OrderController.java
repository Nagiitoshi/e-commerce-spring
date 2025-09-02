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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @Operation(summary = "Criar pedido", description = "Cria um novo pedido para um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(
            @PathVariable Long userId,
            @RequestBody OrderRequestDTO request) {

        OrderResponseDTO response = orderService.createOrder(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar pedidos", description = "Lista todos os pedidos de um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos retornados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> listOrders(@PathVariable Long userId) {
        List<OrderResponseDTO> response = orderService.listOrders(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obter pedido por ID", description = "Retorna detalhes de um pedido pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long orderId) {
        OrderResponseDTO response = orderService.getOrderById(orderId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Atualizar status do pedido", description = "Atualiza o status do pedido (Admin apenas)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Permissão negada"),
            @ApiResponse(responseCode = "404", description = "Pedido ou usuário não encontrado")
    })
    @PutMapping("/admin/{id}/status")
    public Order updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status,
            @RequestParam Long adminUserId) {
        Users adminUser = userRepository.findById(adminUserId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + adminUserId));

        orderService.validatePermissions(adminUser, Role.ADMIN); // validar permissão de admin
        return orderService.updateStatus(id, status);
    }
}
