package com.hao.javaConfig5;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.hao.javaConfig5.component.Comment;
import com.hao.javaConfig5.component.Order;
import com.hao.javaConfig5.component.Product;

/**
 *
 * @author haozhifeng
 * @date 2023/02/23
 */
@Configuration
@Import({MyImportSelector.class, MyDeferredImportSelector.class})
public class AppConfig {
}
