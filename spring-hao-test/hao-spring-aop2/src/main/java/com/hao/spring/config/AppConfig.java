package com.hao.spring.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * AppConfig class
 *
 * @author haozhifeng
 * @date 2023/12/26
 */
@ComponentScan("com.hao.spring")
@Configuration
@EnableAspectJAutoProxy
public class AppConfig {
}
