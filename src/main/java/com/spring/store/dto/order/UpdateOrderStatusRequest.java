package com.spring.store.dto.order;

import com.spring.store.entity.OrderStatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderStatusRequest {
    private Long statusId;
    private OrderStatusCode status;
}
