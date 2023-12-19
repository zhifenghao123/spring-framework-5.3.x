package com.hao.javaConfig9;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.hao.javaConfig9.component.Comment;
import com.hao.javaConfig9.component.Order;
import com.hao.javaConfig9.component.Product;

/**
 *
 * @author haozhifeng
 * @date 2023/02/23
 */
@Configuration
@Import(DbConfig.class)
public class AppConfig {
}
