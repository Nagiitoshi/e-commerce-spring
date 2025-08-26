package com.nagi.e_commerce_spring.dto.auth;

import lombok.Data;

@Data
public class LoginResponseDTO {

    private String token;

    private String refreshToken;
}
