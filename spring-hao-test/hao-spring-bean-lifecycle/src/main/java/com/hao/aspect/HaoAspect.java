package com.hao.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * HaoAspect class
 *
 * @author haozhifeng
 * @date 2023/06/03
 *
 * 猜测Aop实现原理演进：
 * 猜测一：（错误的，经验证）
 * UserService 经过切面之后其实会成生一个代理类
 * class UserService$$EnhancerBySpringCGLIB extends UserService {
 *      public void test() {
 *         1.执行切面逻辑
 *         2.super.test()
 *      }
 * }
 *
 * 最终就是:
 * UserService userService = new UserService$$EnhancerBySpringCGLIB()
 * userService.test();
 *
 * 经过debug，UserService userService = (UserService) applicationContext.getBean("userService"); 对象的orderService为null，
 * 这样的话，super.test()肯定也为null
 * 但是控制台打印出来，执行test()后，打印出的orderService有值，
 * 因此猜测一是错误的
 *
 * 猜测二：（正确的）
 * UserService 经过切面之后其实会成生一个代理类
 * class UserService$$EnhancerBySpringCGLIB extends UserService {
 *
 *      UserService target;
 *
 *      public void test() {
 *         1.执行切面逻辑
 *         2.target.test()
 *      }
 * }
 *
 * 最终就是:
 * UserService userService = new UserService$$EnhancerBySpringCGLIB()
 * userService.target = UserService普通对象
 * （补充Spring Bean生命周期：
 * UserService.class -> 推断构造方法 -> 普通对象 -> 依赖注入 -> afterPropertiesSet()处理 ->初始化后处理(AOP) -> 代理对象 -> Map<beanName,
 * bean对象>单例池
 * ）
 * 也就是Spring会给userService的target属性赋值，赋值为Spring初始的UserService普通对象（也就是未经aop等处理的原始对象，这个对象的属性等等时完整的）
 *
 */

@Component
@Aspect
public class HaoAspect {
    @Before("execution(public void com.hao.service.UserService.test())")
    public void HaoAspectBefore(JoinPoint joinPoint) {
        System.out.println("HaoAspectBefore  execute");
    }
}
