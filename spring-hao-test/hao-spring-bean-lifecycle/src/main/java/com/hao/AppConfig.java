package com.hao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.hao.service.OrderService;

/**
 * AppConfig class
 *
 * @author haozhifeng
 * @date 2023/06/03
 */
@ComponentScan
@EnableAspectJAutoProxy
public class AppConfig {
    /*@Bean
    public OrderService orderService1() {
        return new OrderService();
    }

    @Bean
    public OrderService orderService2() {
        return new OrderService();
    }*/


}
