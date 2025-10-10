package com.spring.store.dao;

import com.spring.store.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemDao extends JpaRepository<Item, Long> {
    Optional<Item> findByName(String name);
}
