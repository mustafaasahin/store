package com.spring.store.dto.order;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class OrderItemResponse {
    Long itemId;
    String itemName;
    Integer quantity;
    BigDecimal unitPrice;
    BigDecimal lineTotal;
}
