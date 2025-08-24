package com.nagi.e_commerce_spring.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is mandatory!!")
    @Size(min = 3, max = 50, message = "Product name must be between 3 and 50 characters!!")
    @Column(nullable = false, length = 50)
    private String name;

    @Size(max = 255, message = "Description can have up to 255 characters!!")
    private String description;

    @NotNull(message = "Product price is mandatory!!")
    @DecimalMin(value = "0.01", inclusive = false, message = "Price must be greater than 0!!")
    @Column(precision = 10)
    private Double price;

    @NotNull(message = "Product quantity is mandatory!!")
    @Min(value = 1, message = "Quantity must be at least 1!!")
    private Integer quantity;

    private String imageUrl;

    private boolean available = true;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
