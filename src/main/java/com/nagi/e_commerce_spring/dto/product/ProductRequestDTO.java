package com.nagi.e_commerce_spring.dto.product;

import lombok.Data;

@Data
public class ProductRequestDTO {

    private String name;

    private String description;

    private Double price;

    private Integer quantity;

    private String imageUrl;

    private boolean available = true;

    private Long categoryId;
}
