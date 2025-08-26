package com.nagi.e_commerce_spring.dto.cart;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemRequestDTO {

    private Long productId;

    private Integer quantity;
}
