package com.hao.spring.aspect;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * AuthAspect class
 *
 * @author haozhifeng
 * @date 2023/12/26
 */
@Component
@Aspect
@Order(1)
public class AuthAspect {
    @Pointcut("execution(* com.hao.spring.service..*.*(..))")
    public void pointCut(){
    }

    @Before(value = "pointCut()")
    public void before(){
        System.out.println("auth success!");
    }
}
