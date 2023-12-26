package com.hao.spring.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * LogAspect class
 *
 * @author haozhifeng
 * @date 2023/12/26
 */
@Component
@Aspect
@Order(2)
public class LogAspect {
    @Pointcut("execution(* com.hao.spring.service..*.*(..))")
    public void pointCut(){
    }

    @Around(value = "pointCut()")
    public Object process(ProceedingJoinPoint proceedingJoinPoint) {
        System.out.println("log:start process ");
        Object result = null;
        try {
            result = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println("log:finish process ");
        return result;
    }
}
