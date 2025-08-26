package com.nagi.e_commerce_spring.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDTO {

    private Long id;

    private String name;

    private String description;

    private Double price;

    private Integer quantity;

    private String imageUrl;

    private boolean available = true;

    private String categoryName; 

    private Long categoryId;
}
