package com.spring.store.service;

import com.spring.store.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public User getUser(String name, String email) {
        return new User(name, email);
    }

    public void saveUser(User user) {
        return;
    }
}
