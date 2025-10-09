package com.spring.store.service;

import com.spring.store.dao.AccountDao;
import com.spring.store.entity.Account;
import com.spring.store.exception.HttpNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountDao accountDao;

    @Transactional(readOnly = true)
    public Account getAccount(String name, String email) {
        return accountDao.findByEmail(email)
                .orElseThrow(() ->
                        new HttpNotFoundException("Account not found for name '" + name + "' and email '" + email + "'."));
    }

    @Transactional
    public void saveAccount(Account account) {
        accountDao.save(account);
    }
}
