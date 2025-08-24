package com.nagi.e_commerce_spring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "addresses")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Address {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @NotBlank(message = "CEP is mandatory!!")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP must follow the pattern 00000-000")
    private String cep;

    @NotBlank(message = "Street is mandatory!!")
    private String street;

    @NotBlank(message = "Number is mandatory!!")
    private String number;

    @NotBlank(message = "Neighborhood is mandatory!!")
    private String neighborhood;

    @NotBlank(message = "City is mandatory!!")
    private String city;

    @NotBlank(message = "State is mandatory!!")
    private String state;  
}
