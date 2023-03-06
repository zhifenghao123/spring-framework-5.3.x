package com.hao.javaConfig2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.hao.javaConfig2.component.Comment;
import com.hao.javaConfig2.component.Order;
import com.hao.javaConfig2.component.Product;

/**
 *
 * @author haozhifeng
 * @date 2023/02/23
 */
@Configuration
@ComponentScan(basePackages = "com.hao.javaConfig2.componentScan")
//@EnableAspectJAutoProxy//(proxyTargetClass = true)
public class AppConfig {
    @Bean
    Product product() {
        Product product = new Product();
        product.setProductId("javaConfig1_procuct_111");
        product.setProductName("orange");
        product.setPrice(3.5);
        return product;
    }

    @Bean
    Order order() {
        Order order = new Order();
        order.setUserAccountId("javaConfig1_userAccount_111");
        order.setOrderId("javaConfig1_orderId_111");
        order.setProduct(product());
        return order;
    }

    @Bean
    Comment comment() {
        Comment comment = new Comment();
        comment.setCommentId("javaConfig1_comment_111");
        comment.setCommentText("很棒");
        comment.setProduct(product());
        return comment;
    }
}
