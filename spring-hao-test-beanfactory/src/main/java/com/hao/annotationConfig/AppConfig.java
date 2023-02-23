package com.hao.annotationConfig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * AppConfig class
 *
 * @author haozhifeng
 * @date 2023/02/23
 */
@ComponentScan("com.hao.annotationConfig")
@Configuration
//@EnableAspectJAutoProxy//(proxyTargetClass = true)
public class AppConfig {
}
