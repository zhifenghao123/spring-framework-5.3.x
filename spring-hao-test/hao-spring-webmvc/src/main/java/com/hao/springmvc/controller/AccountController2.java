package com.hao.springmvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hao.springmvc.service.AccountService;

/**
 * AccountController2 class
 *
 * @author haozhifeng
 * @date 2023/05/20
 */
@Component
@RequestMapping
public class AccountController2 {
    @Autowired
    private AccountService accountService;

    @RequestMapping({"queryAccount2"})
    @ResponseBody
    public String queryAccount() {
        return this.accountService.queryAccount("1");
    }
}
