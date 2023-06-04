package com.hao;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.hao.service.UserService;

/**
 * AppMain class
 *
 * @author haozhifeng
 * @date 2023/06/03
 */
public class AppMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = (UserService) applicationContext.getBean("userService");
        userService.test();

        /*System.out.println(applicationContext.getBean("orderService"));
        System.out.println(applicationContext.getBean("orderService1"));
        System.out.println(applicationContext.getBean("orderService2"));*/
    }
}
