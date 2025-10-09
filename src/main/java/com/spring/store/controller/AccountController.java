package com.spring.store.controller;

import com.spring.store.entity.Account;
import com.spring.store.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

    private AccountService accountService;


    @GetMapping
    public Account getAccount(@RequestParam String name, @RequestParam String email) {
        return accountService.getAccount(name, email);
    }

    @PostMapping
    public ResponseEntity<Void> createAccount(@RequestBody Account account) {
        accountService.saveAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
