package com.nagi.e_commerce_spring.dto.order;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderRequestDTO {

    private List<Long> itemsCartIds;
}
