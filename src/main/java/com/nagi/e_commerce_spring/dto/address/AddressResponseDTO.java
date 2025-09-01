package com.nagi.e_commerce_spring.dto.address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponseDTO {

    private Long id;

    private Long userId;

    private String cep;

    private String street;

    private String number;

    private String neighborhood;

    private String city;

    private String state;
}
