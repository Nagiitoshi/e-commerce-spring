package com.nagi.e_commerce_spring.dto.order;

import java.util.List;

import lombok.Data;

@Data
public class OrderRequestDTO {

    private List<Long> itemsCartIds;
}
