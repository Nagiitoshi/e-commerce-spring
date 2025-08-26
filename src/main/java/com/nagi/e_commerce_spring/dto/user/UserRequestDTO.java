package com.nagi.e_commerce_spring.dto.user;

import lombok.Data;

@Data
public class UserRequestDTO {

    private String username;

    private String password;

    private String email;

    private String phoneNumber;
}
