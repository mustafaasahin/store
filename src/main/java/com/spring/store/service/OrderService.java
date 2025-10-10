package com.spring.store.service;

import com.spring.store.dao.AccountDao;
import com.spring.store.dao.ItemDao;
import com.spring.store.dao.OrderDao;
import com.spring.store.dao.OrderStatusDao;
import com.spring.store.dto.order.CreateOrderRequest;
import com.spring.store.dto.order.OrderItemRequest;
import com.spring.store.dto.order.OrderItemResponse;
import com.spring.store.dto.order.OrderResponse;
import com.spring.store.dto.order.UpdateOrderStatusRequest;
import com.spring.store.entity.Account;
import com.spring.store.entity.Item;
import com.spring.store.entity.Order;
import com.spring.store.entity.OrderItem;
import com.spring.store.entity.OrderStatus;
import com.spring.store.entity.OrderStatusCode;
import com.spring.store.exception.HttpBadRequestException;
import com.spring.store.exception.HttpMethodNotAllowedException;
import com.spring.store.exception.HttpNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderDao orderDao;
    private final AccountDao accountDao;
    private final ItemDao itemDao;
    private final OrderStatusDao orderStatusDao;

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrders(Long accountId) {
        List<Order> orders = accountId == null ? orderDao.findAll() : orderDao.findByAccountId(accountId);
        return orders.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        Order order = findOrderById(id);
        return toResponse(order);
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        validateCreateOrderRequest(request);

        Account account = accountDao.findById(request.getAccountId())
                .orElseThrow(() -> new HttpNotFoundException("Account not found for id '" + request.getAccountId() + "'."));

        OrderStatus initialStatus = orderStatusDao.findByCode(OrderStatusCode.ORDERED)
                .orElseThrow(() -> new HttpNotFoundException("Order status 'ORDERED' is not configured."));

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {
            Item item = itemDao.findById(itemRequest.getItemId())
                    .orElseThrow(() -> new HttpNotFoundException("Item not found for id '" + itemRequest.getItemId() + "'."));

            int quantity = itemRequest.getQuantity();
            BigDecimal lineTotal = item.getPrice().multiply(BigDecimal.valueOf(quantity));

            OrderItem orderItem = OrderItem.builder()
                    .item(item)
                    .quantity(quantity)
                    .unitPrice(item.getPrice())
                    .build();
            orderItems.add(orderItem);
            totalAmount = totalAmount.add(lineTotal);
        }

        Order order = Order.builder()
                .account(account)
                .status(initialStatus)
                .totalAmount(totalAmount)
                .build();

        orderItems.forEach(order::addOrderItem);

        Order saved = orderDao.save(order);
        return toResponse(saved);
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long id, UpdateOrderStatusRequest request) {
        if (request == null || (request.getStatusId() == null && request.getStatus() == null)) {
            throw new HttpBadRequestException("Order status id or code is required.");
        }

        OrderStatus targetStatus;
        if (request.getStatusId() != null) {
            targetStatus = orderStatusDao.findById(request.getStatusId())
                    .orElseThrow(() -> new HttpNotFoundException("Order status id '" + request.getStatusId() + "' is not configured."));
        } else {
            targetStatus = orderStatusDao.findByCode(request.getStatus())
                    .orElseThrow(() -> new HttpNotFoundException("Order status '" + request.getStatus() + "' is not configured."));
        }

        Order order = findOrderById(id);
        order.setStatus(targetStatus);

        return toResponse(orderDao.save(order));
    }

    @Transactional
    public void deleteOrder(Long id) {
        Order order = findOrderById(id);
        orderDao.delete(order);
    }

    private void validateCreateOrderRequest(CreateOrderRequest request) {
        if (request == null) {
            throw new HttpMethodNotAllowedException("Order request payload is required.");
        }

        if (request.getAccountId() == null) {
            throw new HttpMethodNotAllowedException("Account id is required.");
        }

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new HttpMethodNotAllowedException("At least one item is required to create an order.");
        }

        request.getItems().forEach(itemRequest -> {
            if (itemRequest.getItemId() == null) {
                throw new HttpMethodNotAllowedException("Item id is required in order items.");
            }
            if (itemRequest.getQuantity() == null || itemRequest.getQuantity() <= 0) {
                throw new HttpMethodNotAllowedException("Item quantity must be greater than zero.");
            }
        });
    }

    private Order findOrderById(Long id) {
        return orderDao.findById(id)
                .orElseThrow(() -> new HttpNotFoundException("Order not found for id '" + id + "'."));
    }

    private OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .accountId(order.getAccount().getId())
                .statusId(order.getStatus().getId())
                .status(order.getStatus().getCode())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .items(order.getOrderItems().stream()
                        .map(this::toItemResponse)
                        .toList())
                .build();
    }

    private OrderItemResponse toItemResponse(OrderItem orderItem) {
        BigDecimal lineTotal = orderItem.getUnitPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
        return OrderItemResponse.builder()
                .itemId(orderItem.getItem().getId())
                .itemName(orderItem.getItem().getName())
                .quantity(orderItem.getQuantity())
                .unitPrice(orderItem.getUnitPrice())
                .lineTotal(lineTotal)
                .build();
    }
}
