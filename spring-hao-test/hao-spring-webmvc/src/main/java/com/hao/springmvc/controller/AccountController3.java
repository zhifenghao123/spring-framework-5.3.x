package com.hao.springmvc.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;

/**
 * AccountController3 class
 * BeanNameUrlHandler处理的测试
 * 这个类能做的事就相当于@Controller类中的一个@RequestMapping方法可以做的事
 *
 * @author haozhifeng
 * @date 2023/05/20
 */
@Component("/beanNameUrlTest")
public class AccountController3 implements HttpRequestHandler {
    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("/beanNameUrl  处理 AccountController3");
    }
}

