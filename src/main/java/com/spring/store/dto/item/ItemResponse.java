package com.spring.store.dto.item;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class ItemResponse {
    Long id;
    String name;
    String description;
    BigDecimal price;
    Integer stockQuantity;
}
