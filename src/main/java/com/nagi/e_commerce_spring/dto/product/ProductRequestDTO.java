package com.nagi.e_commerce_spring.dto.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductRequestDTO {

    private String name;

    private String description;

    private Double price;

    private Integer quantity;

    private String imageUrl;

    private boolean available = true;

    private Long categoryId;
}
