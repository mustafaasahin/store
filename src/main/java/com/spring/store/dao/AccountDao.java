package com.spring.store.dao;

import com.spring.store.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountDao extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
}
