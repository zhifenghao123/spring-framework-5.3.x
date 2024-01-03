package com.hao.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * UserService class
 *
 * @author haozhifeng
 * @date 2023/12/26
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private AccountService accountService;

    @Override
    public void printUser() {
        System.out.println("UserService##printUser");
        System.out.println("accountService =" + accountService);
        System.out.println("userService =" + this);
    }
}
