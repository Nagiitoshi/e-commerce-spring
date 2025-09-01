package com.nagi.e_commerce_spring.dto.address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressRequestDTO {

    private Long userId;

    private String cep;

    private String street;

    private String number;

    private String neighborhood;

    private String city;
    
    private String state;
}

