package com.hao.javaConfig4;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.hao.javaConfig4.component.Comment;
import com.hao.javaConfig4.component.Order;
import com.hao.javaConfig4.component.Product;

/**
 *
 * @author haozhifeng
 * @date 2023/02/23
 */
@Configuration
@Import({Product.class, Order.class, Comment.class})
public class AppConfig {
}
