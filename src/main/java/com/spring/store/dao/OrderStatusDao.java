package com.spring.store.dao;

import com.spring.store.entity.OrderStatus;
import com.spring.store.entity.OrderStatusCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderStatusDao extends JpaRepository<OrderStatus, Long> {
    Optional<OrderStatus> findByCode(OrderStatusCode code);
}
