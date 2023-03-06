package com.hao.javaConfig6;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.hao.javaConfig6.component.Comment;
import com.hao.javaConfig6.component.Order;
import com.hao.javaConfig6.component.Product;

/**
 *
 * @author haozhifeng
 * @date 2023/02/23
 */
@Configuration
@EnableMyImport(type = 1)
public class AppConfig {
}
