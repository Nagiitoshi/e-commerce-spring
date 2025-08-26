package com.nagi.e_commerce_spring.dto.order;

import java.time.LocalDateTime;
import java.util.List;

import com.nagi.e_commerce_spring.dto.cart.CartItemResponseDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponseDTO {

    private Long id;

    private LocalDateTime data;

    private String status;

    private List<CartItemResponseDTO> items;

    private Double total;
}
