package com.spring.store.service;

import com.spring.store.dao.UserDao;
import com.spring.store.entity.User;
import com.spring.store.exception.HttpNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    @Transactional(readOnly = true)
    public User getUser(String name, String email) {
        return userDao.findByEmail(email)
                .orElseThrow(() ->
                        new HttpNotFoundException("User not found for name '" + name + "' and email '" + email + "'."));
    }

    @Transactional
    public void saveUser(User user) {
        userDao.save(user);
    }
}
