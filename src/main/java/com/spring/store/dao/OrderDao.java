package com.spring.store.dao;

import com.spring.store.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDao extends JpaRepository<Order, Long> {

    @Override
    @EntityGraph(attributePaths = {"orderItems", "orderItems.item", "account", "status"})
    List<Order> findAll();

    @EntityGraph(attributePaths = {"orderItems", "orderItems.item", "account", "status"})
    List<Order> findByAccountId(Long accountId);

    @Override
    @EntityGraph(attributePaths = {"orderItems", "orderItems.item", "account", "status"})
    Optional<Order> findById(Long id);
}
