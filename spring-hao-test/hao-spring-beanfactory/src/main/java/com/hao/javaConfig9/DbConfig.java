package com.hao.javaConfig9;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hao.javaConfig9.component.Order;
import com.hao.javaConfig9.component.Product;

/**
 * DbConfig class
 *
 * @author haozhifeng
 * @date 2023/07/11
 */
@Configuration
public class DbConfig {
    @Bean
    public Product product() {
        return new Product();
    }

    @Bean
    public Order order(Product product) {
        Order order = new Order();
        order.setProduct(product());
        return order;
    }
}
