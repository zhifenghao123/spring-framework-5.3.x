package com.hao.springboot;

/**
 * HaoSpringBootApplication class
 *
 * @author haozhifeng
 * @date 2023/07/01
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.ComponentScan;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ComponentScan
public @interface HaoSpringBootApplication {
}
