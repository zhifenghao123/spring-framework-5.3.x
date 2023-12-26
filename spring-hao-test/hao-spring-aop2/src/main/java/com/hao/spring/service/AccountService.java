package com.hao.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * AccountService class
 *
 * @author haozhifeng
 * @date 2023/12/26
 */
@Service
public class AccountService {
    @Autowired
    private UserService userService;

    public void printAccount() {
        System.out.println("AccountService##printUser");
        System.out.println("accountService =" + this);
        System.out.println("userService =" + userService);
    }

}
