package com.hao.spring.main;

import com.hao.spring.config.AppConfig;
import com.hao.spring.service.AccountService;
import com.hao.spring.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * ApplicationContextMain class
 *
 * @author haozhifeng
 * @date 2023/12/26
 */
public class ApplicationContextMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(AppConfig.class);

        AccountService accountService = (AccountService) applicationContext.getBean("accountService");
        UserService userService = (UserService) applicationContext.getBean("userService");

        accountService.printAccount();

        System.out.println("-----------------");

        userService.printUser();
    }
}
