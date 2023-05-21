package com.hao.springmvc.service;

import org.springframework.stereotype.Service;

/**
 * AccountService class
 *
 * @author haozhifeng
 * @date 2023/04/25
 */
@Service
public class AccountService {
    public String queryAccount(String userId) {
        return "accountName:111";
    }
}
