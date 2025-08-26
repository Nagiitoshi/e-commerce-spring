package com.nagi.e_commerce_spring.dto.cart;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemResponseDTO {

    private Long productId;
    
    private String productName;

    private Integer quantity;

    private Double price;
}
