package com.spring.store.dto.order;

import com.spring.store.entity.OrderStatusCode;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
public class OrderResponse {
    Long id;
    Long accountId;
    Long statusId;
    OrderStatusCode status;
    BigDecimal totalAmount;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    List<OrderItemResponse> items;
}
