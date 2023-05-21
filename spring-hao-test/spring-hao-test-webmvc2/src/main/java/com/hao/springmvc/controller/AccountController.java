package com.hao.springmvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hao.springmvc.service.AccountService;

/**
 * AccountController class
 *
 * @author haozhifeng
 * @date 2023/04/25
 */
@Controller
public class AccountController {
    @Autowired
    private AccountService accountService;

    @RequestMapping({"queryAccount"})
    @ResponseBody
    public String queryAccount() {
        return this.accountService.queryAccount("1");
    }
}
